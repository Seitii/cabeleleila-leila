loginForm.addEventListener('submit', function (e) {
    e.preventDefault(); 

    const email = document.querySelector('.email').value;
    const senha = document.querySelector('.password').value;

    axios.post('http://localhost:8080/api/login', {
        email: email,
        senha: senha
    })
    .then(function (response) {
        console.log(response);
        localStorage.setItem('usuarioNome', response.data.nome);
        localStorage.setItem('usuarioId', response.data.id_usuario);
        window.location.href = '/templates/home.html'; 
    })
    .catch(function (error) {
        console.error(error);
        alert('Falha no login. Verifique seu email e senha.');
    });
});


function togglePassword() {
    const passwordInput = document.querySelector('.input-box .password');
    const passwordIcon = document.getElementById('show-password');
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        passwordIcon.classList.remove('fa-eye-slash');
        passwordIcon.classList.add('fa-eye');
    } else {
        passwordInput.type = 'password';
        passwordIcon.classList.remove('fa-eye');
        passwordIcon.classList.add('fa-eye-slash');
    }
}