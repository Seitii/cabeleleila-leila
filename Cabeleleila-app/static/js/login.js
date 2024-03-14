const url = 'http://localhost:8080/api/usuarios'; 

const usuario = {
  email: "email@dominio.com",
  senha: "senha123",
};

fetch(url, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(usuario)
})
.then(response => response.json())
.then(data => console.log('Success:', data))
.catch((error) => console.error('Error:', error));

// <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>