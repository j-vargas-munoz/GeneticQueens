import java.util.Random;

/**
 * Clase Individual, representa a un individuo (tablero de ajedrez)
 * @implements Comparable
 * @author <a href="mailto:j.vargas.munoz@gmail.com">Jos&eacute; de Jes&uacute;s Vargas Mu&ntilde;oz</a>
 * @version 17/02/2014
 */
public class Individual implements Comparable<Individual> {
    
    private int fenSize;
    private int perfectFitness;
    private float mutationProb;
    private Random rnd;
    private int fenotipe[];
    private int fitness;


    /**
     * Constructor.
     * @param fenSize El tama&ntilde; del fenotipo
     * @param mutationProb La probabiliad de mutaci&oacute;n
     */
    public Individual(int fenSize, float mutationProb) {
        rnd = new Random();
        this.fenSize = fenSize;
        this.mutationProb = mutationProb;
        perfectFitness = (fenSize * (fenSize-1))/2;
        fenotipe = new int[fenSize];
        for (int i = 0; i < fenSize; i++)
            fenotipe[i] = rnd.nextInt(fenSize);
        computeFitness();
    }


    /**
     * Constructor.
     * @param fenotipe El arreglo con el fenotipo del individuo
     * @param mutationProb La probabiliad de mutaci&oacute;n
     */
    public Individual(int[] fenotipe, float mutationProb) {
        rnd = new Random();
        this.fenotipe = fenotipe;
        fenSize = fenotipe.length;
        perfectFitness = (fenSize * (fenSize-1))/2;
        this.mutationProb = mutationProb;
        computeFitness();
    }


    /**
     * Aparea al individuo <code>this</code> con el individuo dado para producir uno nuevo
     * @param x Un individuo a aparear
     * @return Individual El individuo resultante
     */
    public Individual mate(Individual x) {
        int split = rnd.nextInt(fenSize);
        int[] a1 = this.fenotipe;
        int[] a2 = x.getFenotipe();
        int cut = rnd.nextInt(fenSize-1); // Para eliminar el caso en que el hijo es una copia de this
        int[] newFenotipe = new int[fenSize];
        for (int i = 0; i <= cut; i++)
            newFenotipe[i] = a1[i];
        for (int i = cut+1; i < fenSize; i++)
            newFenotipe[i] = a2[i];
        return new Individual(newFenotipe, mutationProb);
    }


    /**
     * Muta al individuo
     */
    public void mutate() {
        for (int i = 0; i < fenSize; i++) {
            float f = rnd.nextFloat();
            if (f <= mutationProb)
                fenotipe[i] = rnd.nextInt(fenSize);
        }
        computeFitness();
    }


    /**
     * Calcula el fitness del individuo
     */
    private void computeFitness() {
        int attackingPairs = 0;
        for (int i = 0; i < fenSize; i++) {
            for (int j = i+1; j < fenSize; j++) {
                if (j - i == Math.abs(fenotipe[i] - fenotipe[j]))  // Reinas en la misma diagonal
                    attackingPairs++;
                if (fenotipe[i] == fenotipe[j]) // Misma línea horizontal
                    attackingPairs++;
            }
        }
        fitness = perfectFitness - attackingPairs;
    }


    /**
     * Devuelve el fenotipo del individuo
     * @return int[] El fenotipo
     */
    public int[] getFenotipe() {
        return fenotipe;
    }


    /**
     * Devuelve el valor fitness del individuo
     * @return int El valor fitness
     */
    public int getFitness() {
        return fitness;
    }


    /**
     * Devuelve el valor del fitness m&aacute;ximo posible para el tama&ntilde;o del fenotipo
     * @return El fitness perfecto
     */
    public int getPerfectFitness() {
        return perfectFitness;
    }


    /**
     * Devuelve una representaci&oacute;n en cadena del individuo
     * @return String La representaci&oacute;n del individuo
     */
    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < fenSize; i++)
            s += fenotipe[i] + (i < fenSize-1 ? ", " : "");
        s += "]. Ataques por pares: " + (perfectFitness - fitness);
        s += ". Fitness: " + fitness;
        return s;
    }


    /**
     * Compara individuos según sus genes
     * @param o Objeto a comparar
     * @return boolean Si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Individual))
            return false;
        int[] oFen = ((Individual)o).getFenotipe();
        if (fenSize != oFen.length)
            return false;
        for (int i = 0; i < fenSize; i++)
            if (fenotipe[i] != oFen[i])
                return false;
        return true;
    }

    /**
     * Compara dos individuos por su valor fitness, para que sean considerados en orden descendente
     * @param i Individuo a comparar
     * @return int Valores contrarios al comparador est&aacute;ndar
     */
    @Override
    public int compareTo(Individual i) {
        int result = ((Integer)fitness).compareTo(i.getFitness());
        if (result == 1)
            return -1;
        if (result == -1)
            return 1;
        return 0;
    }

}