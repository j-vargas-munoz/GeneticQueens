import tad.BinNode;
import tad.BinomialQ;
import java.util.Random;


/**
 * Clase GeneticQueens, controla la evoluci&oacute;n de individuos para encontrar una soluci&oacute;n al problema 
 * de las N reinas
 * @author <a href="mailto:j.vargas.munoz@gmail.com">Jos&eacute; de Jes&uacute;s Vargas Mu&ntilde;oz</a>
 * @version 17/02/2014
 */
public class GeneticQueens {
 
    private int population;   
    private float mutationProb;
    private int elitism;
    private int queens;
    private int limit;
    private int fitnessSum;
    private BinomialQ<Individual> individuals;


    /**
     * Constructor
     * @param population Tama&ntilde;o de la poblaci&oacute;n en cada iteraci&oacute;n
     * @param mutationProb La probabilidad de mutaci&oacute;n de cada individuo
     * @param elitism Los elementos que se conservan intactos en cada iteraci&oacute;n
     * @param queens El n&uacute;mero de reinas a colocar en un tablero de tama&ntilde;o <code>queens</code> x <code>queens</code>
     * @param limit L&iacute;mite de iteraciones para la b&uacute;squeda de la soluci&oacute;n
     */
    public GeneticQueens(int population, float mutationProb, int elitism, int queens, int limit) {
        this.population = population;
        this.mutationProb = mutationProb;
        this.elitism = elitism;
        this.queens = queens;
        this.limit = limit;
        fitnessSum = 0;
        individuals = new BinomialQ<Individual>(population);
        initPopulation();
    }


    /**
     * Inicializa a los individuos de la poblaci&oacute;n
     */
    private void initPopulation() {
        for (int i = 0; i < population; i++) {
            Individual ind = new Individual(queens, mutationProb);
            individuals.insert(ind);
            fitnessSum += ind.getFitness();
        }
    }


    /**
     * Devulve a un individuo de la poblaci&oacute;n usando el comportamiento de 
     * selecci&oacute;n por ruleta.
     * @return Individual El individuo seleccionado
     */
    private Individual rouletteSelection() {
        Random rnd = new Random();
        BinomialQ<Individual> seen = new BinomialQ<Individual>(population);
        Individual current;
        float prob;
        while (!individuals.isEmpty()) {
            current = individuals.deleteMin();
            seen.insert(current);
            prob = (float)current.getFitness() / (float)fitnessSum;
            if (rnd.nextFloat() <= prob) {
                individuals.meld(seen);
                return current;
            }
        }
        individuals.meld(seen);
        return individuals.findMin();
    }


    /**
     * Evoluciona a la poblaci&oacute;n y muestra el estado cada 50 iteraciones
     */
    public void evolve() {
        Individual min = new Individual(queens, mutationProb);
        BinomialQ<Individual> newPopulation;
        int i = 0;
        int newFitnessSum = 0;
        int currentFitness = 0;
        while (i < limit) {
            newPopulation = new BinomialQ<Individual>(population);
            for (int j = 0; j < elitism; j++) {
                currentFitness = individuals.findMin().getFitness();
                fitnessSum -= currentFitness;
                newFitnessSum += currentFitness;
                newPopulation.insert(individuals.findMin());
            }
            while (newPopulation.getSize() < population) {
                Individual i1 = rouletteSelection();
                Individual i2;
                do {
                    i2 = rouletteSelection();
                } while (i1.equals(i2));        // Para que el hijo no sea un clon de i1
                Individual child = i1.mate(i2);
                child.mutate();
                newPopulation.insert(child);
                newFitnessSum += child.getFitness();
            }
            individuals = newPopulation;
            fitnessSum = newFitnessSum;
            newFitnessSum = 0;
            min = individuals.findMin();
            if (min.getFitness() == min.getPerfectFitness()) {
                System.out.println("Solución óptima encontrada en generación: " + i);
                System.out.println("Solución óptima: " + min);
                return;
            }
            if (i % 50 == 0) {
                System.out.println("Mejor solución en iteración " + i + ": " + min);
            }
            i++;
        }
        System.out.println("Se alcanzó el límite de " + limit + " generaciones.");
        System.out.println("Mejor solución encontrada: " + min);
    }

}