import java.util.*;

public class ProcessChainedComparator implements Comparator<Process> {
 
    private List<Comparator<Process>> listComparators;
 
    @SafeVarargs
    public ProcessChainedComparator(Comparator<Process>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }
 
    @Override
    public int compare(Process p1, Process p2) {
        for (Comparator<Process> comparator : listComparators) {
            int result = comparator.compare(p1, p2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}