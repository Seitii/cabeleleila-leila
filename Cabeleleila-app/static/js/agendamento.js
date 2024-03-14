const url = 'http://localhost:8080/api/agendamento'; 

const agendamento = {
  nome: "teste",
  descricao: "corte de cabelo",
  data_agendamento: "2021-10-10T10:00:00.000Z",
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
