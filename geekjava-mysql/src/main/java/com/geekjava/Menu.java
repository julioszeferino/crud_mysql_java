package com.geekjava;

import java.sql.Connection; // importar a classe Connection para conectar ao banco de dados
import java.sql.DriverManager; // importar a classe DriverManager para conectar ao banco de dados
import java.sql.PreparedStatement; // importar a classe PreparedStatement para executar comandos SQL
import java.sql.ResultSet; // importar a classe ResultSet para armazenar o resultado de uma consulta
import java.sql.SQLException; // importar a classe SQLException para tratar erros
import java.util.Scanner;


public class Menu {

	static Scanner teclado = new Scanner(System.in);


    public static Connection conectarBd () {
        System.out.println("Conectando ao banco de dados...");
        Utils utils = new Utils();
		String driver = "com.mysql.jdbc.Driver";
		String url = utils.criaStringConexao();
		try {
			Class.forName(driver); // Verifica se o driver existe
			System.out.println("Driver encontrado com sucesso!");
			System.out.println("Conectando ao banco de dados...");
			System.out.println("URL: " + url);
			System.out.println("Usuario: " + utils.getUsuario());
			System.out.println("Senha: " + utils.getSenha());
			return DriverManager.getConnection(url, utils.getUsuario(), utils.getSenha());
		} catch (Exception e) {
			if(e instanceof ClassNotFoundException) {
				System.out.println("Verifique o driver de conexao." + "\n" + "ERRO: " + e.getMessage());
			} else {
				System.out.println("Verifique se o servidor esta ativo." + "\n" + "ERRO: " + e.getMessage());
			}
			System.exit(-42);
			return null;
		}
    }


    public static void desconectarBd (Connection conn) {
		// throws SQLException: indica que o metodo pode lançar uma exceção do tipo SQLException
        System.out.println("Desconectando do banco de dados...");
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("ERRO: Não foi possível desconectar do banco de dados.");
			e.printStackTrace(System.out);
		}
	}


	public static void listar() {
		System.out.println("Listando produtos...");
		String buscarTodos = "SELECT * FROM produtos";
		try {
			Connection conn = conectarBd();
			PreparedStatement produtos = conn.prepareStatement(buscarTodos);
			// ResultSet.TYPE_SCROLL_INSENSITIVE: permite percorrer o resultado do select para frente e para tras
			// ResultSet.CONCUR_READ_ONLY: permite apenas a leitura do resultado do select
			ResultSet resultado = produtos.executeQuery();

			resultado.last(); // vai para a ultima linha do resultado
			int qtdLinhas = resultado.getRow(); // retorna o numero da ultima linha
			resultado.beforeFirst(); // volta para a primeira linha do resultado

			if (qtdLinhas > 0) {
				System.out.println("Listando Produtos...");
				System.out.println("----------------------");
				while (resultado.next()) { // enquanto houver linhas no resultado
					System.out.println("ID: " + resultado.getInt("id"));
					System.out.println("Produto: " + resultado.getString("nome"));
					System.out.println("Preço: " + resultado.getDouble("preco"));
					System.out.println("Quantidade em Estoque: " + resultado.getInt("estoque"));
					System.out.println("----------------------");
				}
			} else {
				System.out.println("Nao existem produtos cadastrados.");
			}
			System.out.println("Fechando conexão com o banco de dados...");
			produtos.close();
			desconectarBd(conn);
			System.out.println("Conexão com o banco de dados fechada com sucesso!");
			menu(); // retorna ao menu principal
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("ERRO: Não foi possível listar os produtos.");
			System.exit(-42);
		}
	}


	public static void inserir() {
		System.out.println("Inserindo produtos...");
		System.out.println("Informe o nome do produto: ");
		String nome = teclado.nextLine();
		System.out.println("Informe o preço do produto: ");
		double preco = Double.parseDouble(teclado.nextLine());
		System.out.println("Informe a quantidade em estoque: ");
		int quantidade = Integer.parseInt(teclado.nextLine());

		String inserirProduto = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?)";

		try {
			Connection conn = conectarBd();
			PreparedStatement produto = conn.prepareStatement(inserirProduto);
			produto.setString(1, nome);
			produto.setDouble(2, preco);
			produto.setInt(3, quantidade);
			produto.executeUpdate();
			System.out.println("Produto" + nome + "foi inserido com sucesso!");
			System.out.println("Fechando conexão com o banco de dados...");
			produto.close();
			desconectarBd(conn);
			System.out.println("Conexão com o banco de dados fechada com sucesso!");
			menu(); // retorna ao menu principal
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Não foi possível inserir o produto." + "\n" + "ERRO: " + e.getMessage());
			System.exit(-42);
		}
	}


	public static void atualizar() {
		System.out.println("Atualizando produtos...");
		System.out.println("Informe o ID do produto: ");
		int id = Integer.parseInt(teclado.nextLine());

		String buscarProduto = "SELECT * FROM produtos WHERE id = ?";

		try {
			Connection conn = conectarBd();
			PreparedStatement produto = conn.prepareStatement(buscarProduto);
			produto.setInt(1, id);
			ResultSet resultado = produto.executeQuery();

			resultado.last(); // vai para a ultima linha do resultado
			int qtdLinhas = resultado.getRow(); // retorna o numero da ultima linha
			resultado.beforeFirst(); // volta para a primeira linha do resultado

			if (qtdLinhas > 0) {
				System.out.println("Informe o novo nome do produto: ");
				String nome = teclado.nextLine();
				System.out.println("Informe o novo preço do produto: ");
				double preco = Double.parseDouble(teclado.nextLine());
				System.out.println("Informe a nova quantidade em estoque: ");
				int quantidade = Integer.parseInt(teclado.nextLine());

				String atualizarProduto = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?";
				PreparedStatement atualizar = conn.prepareStatement(atualizarProduto);

				atualizar.setString(1, nome);
				atualizar.setDouble(2, preco);
				atualizar.setInt(3, quantidade);
				atualizar.setInt(4, id);

				atualizar.executeUpdate();
				System.out.println("Produto" + nome + "atualizado com sucesso!");
				System.out.println("Fechando conexão com o banco de dados...");
				atualizar.close();
				desconectarBd(conn);
				System.out.println("Conexão com o banco de dados fechada com sucesso!");
				menu(); // retorna ao menu principal
			} else {
				System.out.println("Nao existem produtos cadastrados para o ID informado.");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao atualizar o produto." + "\n" + "ERRO: " + e.getMessage());
			System.exit(-42);
		}
	}


	public static void deletar() {
		System.out.println("Deletando produtos...");
		String buscarProduto = "SELECT * FROM produtos WHERE id = ?";
		String deletarProduto = "DELETE FROM produtos WHERE id = ?";

		System.out.println("Informe o ID do produto a ser deletado: ");
		int id = Integer.parseInt(teclado.nextLine());

		try {
			Connection conn = conectarBd();
			PreparedStatement produto = conn.prepareStatement(buscarProduto);
			produto.setInt(1, id);
			ResultSet resultado = produto.executeQuery();

			resultado.last(); // vai para a ultima linha do resultado
			int qtdLinhas = resultado.getRow(); // retorna o numero da ultima linha
			resultado.beforeFirst(); // volta para a primeira linha do resultado

			if (qtdLinhas > 0) {
				PreparedStatement deletar = conn.prepareStatement(deletarProduto);
				deletar.setInt(1, id);
				deletar.executeUpdate();
				System.out.println("Produto deletado com sucesso!");
				System.out.println("Fechando conexão com o banco de dados...");
				deletar.close();
				desconectarBd(conn);
				System.out.println("Conexão com o banco de dados fechada com sucesso!");
				menu(); // retorna ao menu principal
			} else {
				System.out.println("Nao existem produtos cadastrados para o ID informado.");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro ao deletar o produto." + "\n" + "ERRO: " + e.getMessage());
			System.exit(-42);
		}
	}

	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		System.out.println("0 - Sair do sistema.");

		int opcao = Integer.parseInt(teclado.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		} else if(opcao == 0) {
			System.out.println("Saindo do sistema...");
			System.exit(0);
		}else {
			System.out.println("Opção inválida.");
		}
	}
}