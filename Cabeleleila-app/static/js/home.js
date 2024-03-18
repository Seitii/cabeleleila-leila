document.addEventListener('DOMContentLoaded', function() {
    preencherNomeUsuario();
    listarTodosAgendamentos();

    document.getElementById('form-agendamento').addEventListener('submit', function(e) {
        e.preventDefault(); 
        if (validarFormulario()) {
            adicionarOuEditarAgendamento();
        }
    });

    document.getElementById("plus").addEventListener("click", function() {
        document.querySelector(".modalDialog").style.display = "block";  // procura o id plus e retorna a mensagem. 
    });

    document.getElementById("closeModal").addEventListener("click", function() {
        document.querySelector(".modalDialog").style.display = "none";

        history.pushState("", document.title, window.location.pathname + window.location.search);
    });
});

function preencherNomeUsuario() {
    var nomeUsuario = localStorage.getItem('usuarioNome'); 
    if (nomeUsuario) {
        var campoNome = document.getElementById("nome"); 
        campoNome.value = nomeUsuario;
        campoNome.readOnly = true;
        document.querySelector('.usuario').textContent = nomeUsuario;
    }
}
var agendamentosGlobal = [];

function formatarData(dataISO) {
    const data = new Date(dataISO);
    const dia = data.getDate().toString().padStart(2, '0');
    const mes = (data.getMonth() + 1).toString().padStart(2, '0'); // esta 0 pq começa em janeiro
    const ano = data.getFullYear();
    const horas = data.getHours().toString().padStart(2, '0');
    const minutos = data.getMinutes().toString().padStart(2, '0');
    return `${dia}/${mes}/${ano} ${horas}:${minutos}`;
}

async function listarTodosAgendamentos() {
    try {
        const response = await axios.get('http://localhost:8080/api/agendamento/todos');
        const agendamentos = response.data;
        agendamentosGlobal = agendamentos;
        const listaAgendamentos = document.getElementById('lista-agendamentos');
        listaAgendamentos.innerHTML = '';

        agendamentos.forEach(agendamento => {
            const item = document.createElement('div');
            item.classList.add('agendamento-item');
            const dataFormatada = formatarData(agendamento.data_agendamento);
            item.innerHTML = `
                <p>Cliente: ${agendamento.nomeUsuario}</p>
                <p>Serviço: ${agendamento.descricao}</p>
                <p>Data: ${dataFormatada}</p>
                <button class="edit-btn" data-id="${agendamento.id_agendamento}">Editar</button>
            `;
            listaAgendamentos.appendChild(item);
        });

        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', function() {
                abrirModalEdicao(this.getAttribute('data-id'));
            });
        });
    } catch (error) {
        console.error('Erro ao listar agendamentos:', error);
    }
}

function adicionarAgendamento() {
    var servico = document.getElementById("servico").value;
    var data = document.getElementById("data").value;
    var horario = document.getElementById("horario").value;
    var nomeUsuario = localStorage.getItem('usuarioNome'); 
    var clienteId = localStorage.getItem('usuarioId'); 

    var agendamentoDTO = {
        cliente_id: clienteId,
        descricao: servico,
        data_agendamento: `${data}T${horario}:00`,
        nomeUsuario: nomeUsuario
    };

    axios.post('http://localhost:8080/api/agendamento', agendamentoDTO)
        .then(function(response) {
            console.log(response);
            alert("Agendamento adicionado com sucesso!");

            var modal = document.querySelector(".modalDialog");
            if (modal) {
                modal.style.display = "none";
            }

            limparFormularioAgendamento();

            listarTodosAgendamentos();
        })
        .catch(function(error) {
            console.error(error);
            alert("Erro ao adicionar o agendamento. Por favor, tente novamente.");
        });
}

function limparFormularioAgendamento() {
    document.getElementById("servico").value = '';
    document.getElementById("data").value = '';
    document.getElementById("horario").value = '';
    
}

function validarFormulario() {
    var servico = document.getElementById("servico").value;
    var data = document.getElementById("data").value;
    var horario = document.getElementById("horario").value;

    if (servico === "" || data === "" || horario === "") {
        alert("Por favor, preencha todos os campos.");
        return false;
    }
    
    const isNaMesmaSemana = verificarAgendamentoNaMesmaSemana(data, horario);
    if (!isNaMesmaSemana) {
        return false;
    }

    return true;
}

async function abrirModalEdicao(idAgendamento) {
    try {
        const response = await axios.get(`http://localhost:8080/api/agendamento/${idAgendamento}`);
        const agendamento = response.data;

        document.getElementById('servico').value = agendamento.descricao;
        document.getElementById('data').value = agendamento.data_agendamento.split('T')[0]; // Pega a data e ignora o horário
        document.getElementById('horario').value = agendamento.data_agendamento.split('T')[1].slice(0, 5); // Pega o horário e ignora os segundos
        document.getElementById('nome').value = agendamento.nomeUsuario; 

        document.getElementById('form-agendamento').setAttribute('data-id', idAgendamento);
        document.querySelector(".modalDialog").style.display = "block";
    } catch (error) {
        console.error('Erro ao abrir modal de edição:', error);
    }
}

function adicionarOuEditarAgendamento() {
    const form = document.getElementById('form-agendamento');
    const idAgendamento = document.getElementById('form-agendamento').getAttribute('data-id');
    const servico = document.getElementById("servico").value;
    const data = document.getElementById("data").value;
    const horario = document.getElementById("horario").value;
    const nomeUsuario = localStorage.getItem('usuarioNome');
    const clienteId = localStorage.getItem('usuarioId');

    const agendamentoDTO = {
        cliente_id: clienteId,
        descricao: servico,
        data_agendamento: `${data}T${horario}:00`,
        nomeUsuario: nomeUsuario
    };

    if (!validarDataAgendamento(`${data}T${horario}:00`)) {
        return;
    }

    let url = 'http://localhost:8080/api/agendamento';
    let method = 'post';
    let successMessage = "Agendamento adicionado com sucesso!";
    if (idAgendamento) {
        url += `/${idAgendamento}`;
        method = 'put';
        successMessage = "Agendamento atualizado com sucesso!";
    }

    axios({
        method: method,
        url: url,
        data: agendamentoDTO
    })
    .then(function(response) {
        alert(successMessage);
        document.querySelector(".modalDialog").style.display = "none";
        listarTodosAgendamentos();
    })
    .catch(function(error) {
        console.error(error);
        alert("Erro ao realizar a operação no agendamento.");
    });

    limparFormularioAgendamento();
    form.removeAttribute('data-id');
}

function validarDataAgendamento(dataAgendamento) {
    const dataNovoAgendamento = new Date(dataAgendamento.split('T')[0]);

    for (let agendamento of agendamentosGlobal) {
        const dataAgendamentoExistente = new Date(agendamento.data_agendamento.split('T')[0]);
        
        const diferencaTempo = dataAgendamentoExistente - dataNovoAgendamento;
        const diferencaDias = diferencaTempo / (1000 * 3600 * 24);

        if (diferencaDias >= 0 && diferencaDias < 2) {
            alert("Já existe um agendamento dentro de 2 dias da data selecionada. Escolha outra data.");
            return false;
        }
    }

    return true; 
}

function verificarAgendamentoNaMesmaSemana(dataEscolhida, horarioEscolhido) {
    const dataFormatadaEscolhida = new Date(`${dataEscolhida}T${horarioEscolhido}:00`);
    let agendamentoExistenteProximo = agendamentosGlobal.find(agendamento => {
        let dataAgendamentoExistente = new Date(agendamento.data_agendamento);
        let diferencaDias = (dataFormatadaEscolhida - dataAgendamentoExistente) / (1000 * 3600 * 24);
        
        return diferencaDias >= 0 && diferencaDias <= 7;
    });

    if (agendamentoExistenteProximo) {
        const dataAgendamentoExistente = agendamentoExistenteProximo.data_agendamento.split('T')[0];
        if (confirm(`Já existe um agendamento próximo a esta data em ${dataAgendamentoExistente}. Deseja agendar mesmo assim?`)) {
            document.getElementById('form-agendamento').setAttribute('data-id', agendamentoExistenteProximo.id_agendamento);
        } else {
            document.getElementById('form-agendamento').removeAttribute('data-id');
        }
        return true;
    }
    document.getElementById('form-agendamento').removeAttribute('data-id');
    return true;
}