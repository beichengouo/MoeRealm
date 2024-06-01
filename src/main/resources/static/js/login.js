console.clear();

const loginBtn = document.getElementById('login');
const signupBtn = document.getElementById('signup');

loginBtn.addEventListener('click', (e) => {
    let parent = e.target.parentNode.parentNode;
    Array.from(e.target.parentNode.parentNode.classList).find((element) => {
        if(element !== "slide-up") {
            parent.classList.add('slide-up')
        }else{
            signupBtn.parentNode.classList.add('slide-up')
            parent.classList.remove('slide-up')
        }
    });
});

signupBtn.addEventListener('click', (e) => {
    let parent = e.target.parentNode;
    Array.from(e.target.parentNode.classList).find((element) => {
        if(element !== "slide-up") {
            parent.classList.add('slide-up')
        }else{
            loginBtn.parentNode.parentNode.classList.add('slide-up')
            parent.classList.remove('slide-up')
        }
    });
});

// 获取发送验证码按钮和注册按钮
const sendCodeBtn = document.querySelector('.signup .submit-btn:nth-of-type(1)');
const registerBtn = document.querySelector('.signup .submit-btn:nth-of-type(2)');

// 发送验证码
sendCodeBtn.addEventListener('click', () => {
    const email = document.getElementById('email').value;
    const username = document.querySelector('.signup input[type="text"]').value;
    const password = document.querySelector('.signup input[type="password"]').value;

    // 检查邮箱格式是否正确
    if (!validateEmail(email)) {
        alert('请输入正确的邮箱格式');
        return;
    }

    // 检查用户名格式是否正确
    if (!validateUsername(username)) {
        alert('用户名必须为6位以上数字或字母');
        return;
    }

    // 检查密码格式是否正确
    if (!validatePassword(password)) {
        alert('密码必须为6位以上数字和字母混合');
        return;
    }

    // 显示发送中消息
    alert('正在努力发送中...');

    fetch('/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 发送成功后显示成功消息
                alert('验证码已发送至您的邮箱，请注意查收！');
            } else {
                // 发送失败显示失败消息
                alert('发送验证码失败：' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 发送失败显示错误消息
            alert('发送验证码失败：' + error.message);
        });
});

function validateUsername(username) {
    // 用户名必须为6位以上数字或字母
    const re = /^[a-zA-Z0-9]{6,}$/;
    return re.test(username);
}

function validatePassword(password) {
    // 密码必须为6位以上数字和字母混合
    const re = /^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,}$/;
    return re.test(password);
}



// 邮箱格式验证函数
function validateEmail(email) {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
}


// 注册
registerBtn.addEventListener('click', () => {
    const username = document.querySelector('.signup input[type="text"]').value;
    const password = document.querySelector('.signup input[type="password"]').value;
    const email = document.getElementById('email').value;
    const code = document.getElementById('code').value;

    // 检查验证码是否填写
    if (code === '') {
        alert('请输入验证码');
        return;
    }

    fetch(`/users/verify?email=${email}&code=${code}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 验证成功，继续注册
                fetch('/users/completeRegistration', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: username,
                        email: email,
                        password: password
                    })
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert('注册成功！');
                        } else {
                            alert('注册失败：' + data.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('注册失败：' + error.message);
                    });
            } else {
                alert('验证码错误！');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('验证码验证失败：' + error.message);
        });
});


// 登录按钮点击事件处理函数
document.querySelector('.login .submit-btn').addEventListener('click', () => {
    const username = document.querySelector('.login input[type="text"]').value;
    const password = document.querySelector('.login input[type="password"]').value;

    // 发送登录请求
    fetch('/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username: username, password: password })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 登录成功，保存用户信息到localStorage
                localStorage.setItem('username', username);
                // 跳转到首页或其他页面
                alert('登录成功！');
                window.location.href = '/home.html'; // 跳转到主站
            } else {
                // 登录失败，显示失败消息
                alert('登录失败：' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('登录失败：' + error.message);
        });
});
