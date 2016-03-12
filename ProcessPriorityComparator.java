import java.util.Comparator;
 
/**
 * This comparator compares two employees by their ages.
 */
public class ProcessPriorityComparator implements Comparator<Process> {
 
    @Override
    public int compare(Process p1, Process p2) {
        return p1.getPriority() - p2.getPriority();
    }
}