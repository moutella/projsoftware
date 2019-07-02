
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import corejava.Console;
import modelo.ModelosCarro;
import modelo.Carro;
import servico.ModelosCarroAppService;

public class PrincipalModelosCarro {
	public static void main(String[] args) {
		String nome;
		
		ModelosCarro umModelosCarro;
	
		@SuppressWarnings("resource")
		ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");

		
		ModelosCarroAppService modelosCarroAppService = (ModelosCarroAppService)fabrica.getBean ("modelosCarroAppService");
		
		boolean continua = true;
		while(continua) {
			System.out.println('\n' + "O que voc� deseja fazer?");
		    System.out.println('\n' + "1. Cadastrar um modelo");
		    System.out.println("2. Alterar um modelo");
		    System.out.println("3. Remover um modelo");
		    System.out.println("4. Listar todos os modelos e seus carros");
		    int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 4:");

		    switch (opcao) {
		    case 1:{
		    	nome = Console.readLine('\n' + "Informe o nome do modelo: ");
		    	umModelosCarro = new ModelosCarro(nome);
		    	
		    	try {
		    		modelosCarroAppService.inclui(umModelosCarro);
		    	} catch(Exception e) {
		    		System.out.println(e.getMessage());
		    	}
		    	break;
		    }
		    case 2:
		    {
		    	int resposta = Console.readInt('\n' + "Digite o n�mero do modelo que voc� deseja alterar: ");
		    	try {
		    		umModelosCarro = modelosCarroAppService.recuperaUmModelosCarro(resposta);
		    	} catch(Exception e) {
		    		System.out.println("\n" + e.getMessage());
		    		break;
		    	}
		    	System.out.println("\nN�mero: "+ umModelosCarro.getId() +"Nome =" + umModelosCarro.getNome());
		    	String novoNome = Console.readLine(("\nDigite o novo nome"));
		    	umModelosCarro.setNome(novoNome);
		    	try {
		    		modelosCarroAppService.altera(umModelosCarro);
		    	} catch (Exception e) {
		    		System.out.println("\n" + e.getMessage());
		    		break;
		    	}
		    	break;
		    }
		    case 3:{
		    	int resposta = Console.readInt('\n' + "Digite o n�mero do modelo que voc� deseja remover: ");

				try {
				    umModelosCarro = modelosCarroAppService.recuperaUmModelosCarro(resposta);
				} catch (Exception e) {
				    System.out.println('\n' + e.getMessage());
				    break;
				}

				System.out.println('\n' + "N�mero = " + umModelosCarro.getId() + "    Nome = " + umModelosCarro.getNome());

				String resp = Console.readLine('\n' + "Confirma a remo��o do modelo?");

				if (resp.toLowerCase().equals("s")) {
				    try {
					modelosCarroAppService.exclui(umModelosCarro.getId());
					System.out.println('\n' + "Modelo removido com sucesso!");
				    } catch (Exception e) {
					System.out.println('\n' + e.getMessage());
				    }
				} else {
				    System.out.println('\n' + "Modelo n�o removido.");
				}

				break;
		    }
		    case 4:
		    {
		    	List<ModelosCarro> modelosCarros = modelosCarroAppService.recuperaModelosCarrosECarros();
		    	if(modelosCarros.size() != 0) {
		    		System.out.println("");
		    		for(ModelosCarro modelosCarro : modelosCarros) {
		    			System.out.println("ID: " + modelosCarro.getId());
		    			System.out.println("Nome: " + modelosCarro.getNome());
		    			List<Carro> carros = modelosCarro.getCarros();
		    			for(Carro carro : carros) {
		    				System.out.println("Placa: "+ carro.getPlaca());
		    				//System.out.println("DATA CADASTRO: " + carro.getDataCriacaoMasc());
		    			}
		    		}
		    		
		    	}else {
				    System.out.println('\n' + "N�o h� carros cadastrados.");
				}
		    	break;
		    }
		}
		
		
	}
	}
}
