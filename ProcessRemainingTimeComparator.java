import java.util.Comparator;
 
/**
 * This comparator compares two employees by their ages.
 */
public class ProcessRemainingTimeComparator implements Comparator<Process> {
 
    @Override
    public int compare(Process p1, Process p2) {
        return p1.getRemainingTime() - p2.getRemainingTime();
    }
}