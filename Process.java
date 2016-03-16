public class Process
{
	private int processNumber;
	private int arrivalTime;
	private int burstTime;
	private int remainingTime;
	private int priority;
	private boolean isNewProcess;

	public Process(int pn, int a, int b, int p)
	{
		processNumber = pn;
		arrivalTime = a;
		burstTime = remainingTime = b;
		priority = p;
		isNewProcess = true;
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

	public boolean isNewProcess()
	{
		return isNewProcess;
	}

	public void setIsNewProcess(boolean b)
	{
		isNewProcess = b;
	}

}