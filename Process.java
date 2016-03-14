public class Process
{
	private int processNumber;
	private int arrivalTime;
	private int burstTime;
	private int remainingTime;
	private int priority;
	private boolean insideOfQueue;

	public Process(int pn, int a, int b, int p)
	{
		processNumber = pn;
		arrivalTime = a;
		burstTime = remainingTime = b;
		priority = p;
		insideOfQueue = false;
	}

	public int getArrivalTime()
	{
		return arrivalTime;
	}

	public int getProcessNumber()
	{
		return processNumber;
	}

	public int getBurstTime()
	{
		return burstTime;
	}

	public int getRemainingTime()
	{
		return remainingTime;
	}

	public void runUntil(int t)
	{
		remainingTime -= t;
	}

	public int getPriority()
	{
		return priority;
	}

	public boolean isInsideOfQueue()
	{
		return insideOfQueue;
	}

	public void setInsideOfQueue(boolean b)
	{
		insideOfQueue = b;
	}

}