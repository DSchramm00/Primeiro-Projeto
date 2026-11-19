const btnCadastrar = document.getElementById("btnCadastrar");
const listaProdutos = document.getElementById("listaProdutos");

btnCadastrar.addEventListener("click", cadastrarProduto);

async function cadastrarProduto() {
    const nome = document.getElementById("nome").value;
    const valor = Number(document.getElementById("valor").value);
    const codigo = Number(document.getElementById("codigo").value);

    const produto = {
        nome,
        valor,
        codigo
    };

    const resposta = await fetch("http://localhost:3000/produtos", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(produto)
    });

    console.log(await resposta.json());

    carregarProdutos();
}

async function carregarProdutos() {
    const resposta = await fetch("http://localhost:3000/produtos");

    const produtos = await resposta.json();

    listaProdutos.innerHTML = "";

    produtos.forEach(produto => {
        const item = document.createElement("p");

        item.textContent =
            `${produto.nome} - R$ ${produto.valor} - Código: ${produto.codigo}`;

        listaProdutos.appendChild(item);
    });
}

carregarProdutos();