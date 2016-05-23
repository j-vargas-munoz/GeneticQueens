/**
 * Clase QueensSolver, clase de uso para resolver el problema de las N reinas.
 * @author <a href="mailto:j.vargas.munoz@gmail.com">Jos&eacute; de Jes&uacute;s Vargas Mu&ntilde;oz</a>
 * @version 17/02/2014
 */
public class QueensSolver {

    private static final int N_PARAMS = 5;
    private static final String CORRECT_ENTRY = "La invocación correcta al programa es: java QueensSolver pop mp el qu li\n"
                                                    + "pop: Tamaño de la población >= 2\n"
                                                    + "mp: Probabilidad de mutación entre 0 y 1\n"
                                                    + "el: Elitismo\n"
                                                    + "qu: Tamaño del tablero, así como el número de reinas a colocar\n"
                                                    + "li: Límite de iteraciones para encontrar la solución";


    /**
     * M&eacute;todo main
     * @param args Argumentos del main
     */
    public static void main(String[] args) {
        int pop, el, qu, li;
        float mp;
        GeneticQueens gq;
        if (args.length != 5) {
            System.out.println(CORRECT_ENTRY);
            return;
        }
        try {
            pop = Integer.parseInt(args[0]);
            mp = Float.parseFloat(args[1]);
            el = Integer.parseInt(args[2]);
            qu = Integer.parseInt(args[3]);
            li = Integer.parseInt(args[4]);
        } catch (Exception e) {
            System.out.println(CORRECT_ENTRY);
            return;
        }
        if (pop < 2) {
            System.out.println("No hay suficientes individuos para la cruza");
            return;
        }
        if (mp < 0 || mp > 1) {
            System.out.println("La probabilidad debe estar entre 0 y 1");
            return;
        }
        if (el < 0 || el >= pop) {
            System.out.println("Valor de elitismo no permitido");
            return;
        }
        if (qu <= 3) {
            System.out.println("Para éste tamaño de tablero, el problema no tiene solución");
            return;
        }
        if (li < 0) {
            System.out.println("Valor límite no permitido");
            return;
        }
        gq = new GeneticQueens(pop, mp, el, qu, li);
        gq.evolve();
    }
    
}