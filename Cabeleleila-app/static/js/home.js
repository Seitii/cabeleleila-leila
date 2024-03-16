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
        const response = await axios.get('http://localhost:8080/api/agendamento/todos');  // API GET ( Lista todos os agendamentos)
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

    axios.post('http://localhost:8080/api/agendamento', agendamentoDTO)  // API POST (Cria um os agendamentos)
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
    
    // Verifica se está na mesma semana e a resposta do usuarioo
    const isNaMesmaSemana = verificarAgendamentoNaMesmaSemana(data, horario);
    if (!isNaMesmaSemana) {
        // não realiza nada por enquanto ( sem ideia no momento)
        return false;
    }

    return true;
}

async function abrirModalEdicao(idAgendamento) {
    try {
        const response = await axios.get(`http://localhost:8080/api/agendamento/${idAgendamento}`);    // API GET ( Busca o agendamento por ID)
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

    let url = 'http://localhost:8080/api/agendamento';   // API POST ( Cria os agendamentos)
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


function estaNaMesmaSemana(data1, data2) {
    const startOfWeek = date => new Date(date.setDate(date.getDate() - date.getDay() + (date.getDay() === 0 ? -6 : 1))); // Ajusta para a segunda-feira como o primeiro dia da semana
    const endOfWeek = date => new Date(date.setDate(date.getDay() === 0 ? date.getDate() : date.getDate() + (7 - date.getDay())));

    let semanaInicio1 = startOfWeek(new Date(data1));
    let semanaFim1 = endOfWeek(new Date(data1));

    return new Date(data2) >= semanaInicio1 && new Date(data2) <= semanaFim1;
}

function verificarAgendamentoNaMesmaSemana(dataEscolhida, horarioEscolhido) {
    const dataFormatadaEscolhida = `${dataEscolhida}T${horarioEscolhido}:00`;
    let agendamentoExistenteNaSemana = agendamentosGlobal.find(agendamento => 
        estaNaMesmaSemana(new Date(agendamento.data_agendamento), new Date(dataFormatadaEscolhida)));

    if (agendamentoExistenteNaSemana) {
        const dataAgendamentoExistente = agendamentoExistenteNaSemana.data_agendamento.split('T')[0];
        if (confirm(`Já existe um agendamento para esta semana em ${dataAgendamentoExistente}. Deseja agendar para a mesma data?`)) {

            document.getElementById('data').value = dataAgendamentoExistente;   // adiciona a data no formulario
            // Adiciona o ID do agendamento existente para o formulário 
            document.getElementById('form-agendamento').setAttribute('data-id', agendamentoExistenteNaSemana.id_agendamento);
            adicionarOuEditarAgendamento();
            return false; 
        } else {
            // Se caso escolher não, vai adicionar na data escolhida e remove o id existente
            document.getElementById('form-agendamento').removeAttribute('data-id');
            return true; 
        }
    }
    document.getElementById('form-agendamento').removeAttribute('data-id');
    return true; 
}