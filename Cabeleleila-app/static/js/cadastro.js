document.addEventListener('DOMContentLoaded', function () {
    const cadastroForm = document.getElementById('cadastroForm');

    cadastroForm.addEventListener('submit', function (e) {
        e.preventDefault(); 

        const nome = document.querySelector('.input-box .nome').value;
        const email = document.querySelector('.input-box .email').value;
        const senha = document.querySelector('.input-box .password').value;

        axios.post('http://localhost:8080/api/usuarios', {
            nome: nome,
            email: email,
            senha: senha
        })
        .then(function (response) {
            console.log(response);
            alert('Cadastro realizado com sucesso!');
            window.location.href = '/templates/login.html';
        })
        .catch(function (error) {
            console.error(error);
            alert('Falha no cadastro!');
        });
    });
});