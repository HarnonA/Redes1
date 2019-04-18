import java.util.Scanner;

public class Velha {

	static int velha[][];

	/**
	 * @param args
	 */

	//public static void main(String[] args) {
	public Velha(){
		// TODO Auto-generated method stub

		// Interface interfa = new Interface();
		int linha, coluna;

		int vez = 0;
		linha = 0;
		coluna = 0;

		// inicializa a velha
		velha = new int[3][3];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				velha[i][j] = 0;

		imprime();
		// no maximo 9 jogadas sao permitidas
		while (vez < 9) {

			// lê linha e coluna da posiçao na velha
			Scanner s = new Scanner(System.in);

			System.out.print("Escolha linha: ");
			linha = s.nextInt();
			System.out.print("Escolha coluna: ");
			coluna = s.nextInt();

			try {
				// certifica que posiçao ja nao foi usada
				while (velha[linha][coluna] != 0) {
					System.out.println(" \n* Jogada invalida *");
					System.out.print("Escolha linha: ");
					linha = s.nextInt();
					System.out.print("Escolha coluna: ");
					coluna = s.nextInt();

				}
			} catch (Exception e) {
				System.out
						.println("Voce digitou um numero nao permitido :(\n\n");
			}

			// 'x' para jogador 1
			// 'o' para jogador 2

			if ((vez % 2) == 0)
				velha[linha][coluna] = 1;
			else
				velha[linha][coluna] = -1;

			if (checar()) {
				imprime();
				vez++;
			} else
				vez = 9;

		}
		System.out.println("\nPartida acabou");

	}

	public static boolean checar() {
		int soma = 0;

		// verifica vencedor por coluna
		for (int i = 0; i < 3; i++) {
			soma = velha[i][0] + velha[i][1] + velha[i][2];
			if (soma == 3) {
				System.out.println("Jogador1 venceu");
				return false;
			} else if (soma == -3) {
				System.out.println("Jogador2 venceu");
				return false;
			}
			soma = 0;
		}

		// verifica vencedor por coluna
		for (int j = 0; j < 3; j++) {
			soma = velha[0][j] + velha[1][j] + velha[2][j];
			if (soma == 3) {
				System.out.println("Jogador1 venceu");
				return false;
			} else if (soma == -3) {
				System.out.println("Jogador2 venceu");
				return false;
			}
			soma = 0;
		}

		// vencedor na diagonal principal
		soma = velha[0][0] + velha[1][1] + velha[2][2];
		if (soma == 3) {
			System.out.println("Jogador1 venceu");
			return false;
		} else if (soma == -3) {
			System.out.println("Jogador2 venceu");
			return false;
		}
		soma = 0;

		// vencedor na diagonal secundaria
		soma = velha[0][2] + velha[1][1] + velha[2][0];
		if (soma == 3) {
			System.out.println("Jogador1 venceu");
			return false;
		} else if (soma == -3) {
			System.out.println("Jogador2 venceu");
			return false;
		}
		soma = 0;

		return true;

	}

	public static void imprime() {
		// imprime a velha
		for (int i = 0; i < 3; i++) {
			System.out.println();
			if (i == 1 || i == 2)
				System.out.println("-----");
			for (int j = 0; j < 3; j++) {
				if (j == 1 || j == 2)
					System.out.print("|");

				if (velha[i][j] == 0)
					System.out.print(" ");
				if (velha[i][j] == 1)
					System.out.print("X");
				if (velha[i][j] == -1)
					System.out.print("0");
				// System.out.print(velha[i][j]);
			}
		}
		System.out.println();

	}

}
