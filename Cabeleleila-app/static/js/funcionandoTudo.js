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
        document.querySelector(".modalDialog").style.display = "block";
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
            item.innerHTML = `
                <p>Cliente: ${agendamento.nomeUsuario}</p>
                <p>Serviço: ${agendamento.descricao}</p>
                <p>Data: ${agendamento.data_agendamento}</p>
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

    // Primeiro, verifica a regra dos 2 dias antes de prosseguir com outras validações
    if (!validarDataAgendamento(`${data}T${horario}:00`)) {
        return false; // Se não passar na validação da data, interrompe a execução aqui
    }

    verificarAgendamentoNaMesmaSemana(data, horario);

    return true;
}

async function abrirModalEdicao(idAgendamento) {
        console.log("Tentando abrir modal de edição para o agendamento com ID:", idAgendamento);
    try {
        const response = await axios.get(`http://localhost:8080/api/agendamento/${idAgendamento}`);
        const agendamento = response.data;

        document.getElementById('servico').value = agendamento.descricao;
        document.getElementById('data').value = agendamento.data_agendamento.split('T')[0];
        document.getElementById('horario').value = agendamento.data_agendamento.split('T')[1].slice(0, 5);
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

    let url = 'http://localhost:8080/api/agendamento';
    let method = 'post';
    let successMessage = "Agendamento adicionado com sucesso!";

    if (idAgendamento) {
        if (!validarDataAgendamento(`${data}T${horario}:00`)) {
            return; 

        }
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
    const dataAtual = new Date();
    dataAtual.setHours(0, 0, 0, 0); 

    const dataFormatada = new Date(dataAgendamento.split('T')[0]); 

    const diferencaTempo = dataFormatada.getTime() - dataAtual.getTime();
    const diferencaDias = diferencaTempo / (1000 * 3600 * 24);

    if (diferencaDias < 2) {
        alert("Alteração de agendamento permitida apenas até 2 dias antes da data agendada. Por favor, entre em contato por telefone para alterações.");
        return false;
    }

    return true;
}

function verificarAgendamentoNaMesmaSemana(data, horario) {
    const dataFormatada = new Date(`${data}T${horario}:00`);

    for (let i = 0; i < agendamentosGlobal.length; i++) {
        let agendamento = agendamentosGlobal[i];
        let dataAgendamentoExistente = new Date(agendamento.data_agendamento);
        let dataAgendamentoNovo = new Date(dataFormatada);

        // Adiciona 7 dias à data do agendamento existente
        let umaSemanaAposAgendamentoExistente = new Date(dataAgendamentoExistente);
        umaSemanaAposAgendamentoExistente.setDate(dataAgendamentoExistente.getDate() + 7);

        // Verifica se a nova data de agendamento está antes de uma semana após a data do agendamento existente
        if (umaSemanaAposAgendamentoExistente >= dataAgendamentoNovo) {
            if (window.confirm(`Já existe um agendamento para esta semana em ${agendamento.data_agendamento}. Deseja agendar para a mesma data?`)) {
                // Define o ID do agendamento existente no formulário para que seja atualizado.
                document.getElementById('form-agendamento').setAttribute('data-id', agendamento.id_agendamento);
            }
            break; // Encerra o loop se um agendamento na mesma semana for encontrado
        }
    }
}