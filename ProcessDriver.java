import java.util.*;

public class ProcessDriver
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt(); // test cases
		sc.nextLine(); // lmao

		for(int i = 1; i <= T; i++)
		{
			String input = sc.nextLine();
			String[] arg = input.split(" ");

			System.out.println(i);

			List<Process> process = new ArrayList<Process>();
			for(int j = 1; j <= Integer.parseInt(arg[0]); j++)
			{
				String inputProcess = sc.nextLine();
				String[] arg1 = inputProcess.split(" ");
				int a = Integer.parseInt(arg1[0]);
				int b = Integer.parseInt(arg1[1]);
				int p = Integer.parseInt(arg1[2]);

				process.add(new Process(j,a,b,p));
			}

			switch(arg[1])
			{
				case "FCFS":
				{
					
					System.out.print(FCFS(process));
					break;
				}
				case "SJF":
				{
					String fin =  SJF(process);
					System.out.print(fin);
					break;
				}
				case "SRTF":
				{
					String output = SRTF(process);
					String formattedOutput = toString(combine(output,false));
					System.out.print(formattedOutput);
					break;
				}
				case "P":
				{
					String output = P(process);
					String formattedOutput = toString(combine(output,false));
					System.out.print(formattedOutput);
					break;
				}
				case "RR":
				{
					String output = RR(process,Integer.parseInt(arg[2]));
					String formattedOutput = toString(combine(output,false));
					System.out.print(formattedOutput);
					//System.out.print(RR(process,Integer.parseInt(arg[2])));
					break;
				}
			}

		}
	}

	// First Come First Served
	public static String FCFS(List<Process> p)
	{
		// Sorts the processes by Arrival Time first, then Priority
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(), new ProcessPriorityComparator()));
		String output = "";
		int currentTime = p.get(0).getArrivalTime(); // gets the arrival time of the first process

		// while the list is not empty
		while(p.isEmpty()!=true)
		{
			// if the first process's arrival time is less than current time, get current time;
			if(p.get(0).getArrivalTime() <= currentTime)
			{
				output += currentTime;
			}

			// else, get the arrival time
			else
			{
				output += p.get(0).getArrivalTime();
			}
			output += " ";
			output += p.get(0).getProcessNumber(); // get the current process number
			output += " ";
			output += p.get(0).getBurstTime();  // get the current process burst time

			// Since this is FCFS, we finish the current process. 
			p.get(0).runUntil(p.get(0).getBurstTime());
			currentTime += p.get(0).getBurstTime(); // current time is updated to current time + current process burst time

			// if current process's remaining time is equal to zero. remove from list
			if(p.get(0).getRemainingTime() == 0)
			{
				output += "X\n";
				p.remove(0);
			}
		}

		return output;
	}

	//Shortest Job First, non­preemptive
	public static String SJF(List<Process> p)
	{
		// has a Temporary list.
		List<Process> temp = new ArrayList<Process>();
		// Sorts the processes by Arrival Time first, then Remaining Time
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessRemainingTimeComparator()));
		String output = "";
		int currentTime = p.get(0).getArrivalTime(); // sets current time to the arrival time of the first process 


		temp.add(p.get(0)); // adds the first process from P to Temp.
		p.remove(0); //remove the first process from P

		// run until Temp has elements
		while(temp.isEmpty()!=true)
		{
			output += currentTime; 
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";
			output += temp.get(0).getRemainingTime();

			// Since this is SJF, we finish the current process by getting the remaining time of the first process in Temp
			currentTime += temp.get(0).getRemainingTime(); //update current time by adding the remaining time of the first process in Temp to it
			temp.get(0).runUntil(temp.get(0).getRemainingTime()); // run the process until it finishes.

			// if the remaining time of the first process is less than or equal to zero
			if(temp.get(0).getRemainingTime() <= 0)
			{
				output += "X\n";
				temp.remove(0); // remove the first process from the list

				// if Temp list is not empty
				if(temp.isEmpty() != true)
				{
					// if current time is less than to the first process of temp 
					if(currentTime < temp.get(0).getArrivalTime())
					{
						currentTime = temp.get(0).getArrivalTime(); // set current time to the arrival time of temp.get(0) 
					}
				}

				// if Temp list is empty AND p list is not empty
				if(temp.isEmpty() == true && p.isEmpty() != true)
				{
					// if current time is less than to the first process of p 
					if(currentTime < p.get(0).getArrivalTime())
					{
						currentTime = p.get(0).getArrivalTime(); // set current time to the arrival time of p.get(0)
					}
				}
			}

			// go through P backwards. if the processes' arrival time is less than or equal to current time.
			// add to Temp. Remove the process from P.
			for (int i = p.size()-1; i >= 0; i--){
			    if ( p.get(i).getArrivalTime() <= currentTime)
			    {
			    	temp.add(p.get(i));
			        p.remove(i);
			    }
			 }

			// since we go went through P backwards. we have to sort the list Temp
			// by ProcessRemainingTime, then ArrivalTime, then Priority
			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessRemainingTimeComparator(),new ProcessArrivalTimeComparator(), new ProcessPriorityComparator()));
		}
		return output;
	}

	//SRTF (Shortest Remaining Time First, SJF preemptive)
	public static String SRTF(List<Process> p)
	{
		// has a Temporary list.
		List<Process> temp = new ArrayList<Process>();
		// Sorts the processes by Arrival Time first, then Remaining Time
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessRemainingTimeComparator()));
		String output = "";

		temp.add(p.get(0)); // adds the first process from P to Temp.
		p.remove(0); //remove the first process from P

		int currentTime = temp.get(0).getArrivalTime(); 
		int elapsed = 0;
		int newCurrentTime = 0;
		int curProcess= temp.get(0).getProcessNumber();
		int startTime = 0;
		int strTime = 0;
		int strtProcNum = 0;
		int prevProcess = -1;

		// run until Temp has elements
		while(temp.isEmpty()!=true)
		{
			output += currentTime; // start
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";

			// if P has elements
			if(p.size() >0)
			{
				//check which is the next time interval
				// get the minimum of (Current time plus remaining time of Temp.get(0) ) 
				//and the arrival time of the first entry of P
				newCurrentTime = Math.min(currentTime + temp.get(0).getRemainingTime(), p.get(0).getArrivalTime());
			}

			// if P has no elements
			else
			{
				// update new current time by adding the remaining time of P to it
				newCurrentTime += temp.get(0).getRemainingTime();
			}

			// time is difference of new current time and current time
			int time = newCurrentTime - currentTime;
			currentTime = newCurrentTime; // set current time to new current time
			elapsed += time;
			temp.get(0).runUntil(time); // run the first Process in Temp for time
			
			prevProcess = curProcess; // previous process = current process (for the next loop)

			// go through P backwards. if the processes' arrival time is less than or equal to current time.
			// add to Temp. Remove the process from P.
			for (int i = p.size()-1; i >= 0; i--){
				if ( p.get(i).getArrivalTime() <= currentTime)
				{
				   	temp.add(p.get(i));
				    p.remove(i);
				}
			}
		
			// since we go went through P backwards. we have to sort the list Temp
			// by ProcessRemainingTime, then then Priority
			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessRemainingTimeComparator(), new ProcessPriorityComparator()));

			int index = 0;

			//since we sorted, the order may be different
			// find the index of the first process of Temp pre-sorting
			for(int i = 0; i < temp.size(); i++)
			{
				if(temp.get(i).getProcessNumber() == curProcess)
				{
					index =i;
				}
			}

			output += time;

			// If the first process of Temp pre-sorting has a remaining time of less than or equal to 0
			// remove the process from Temp
			if(temp.get(index).getRemainingTime() <= 0)
			{
				output += "X";
				temp.remove(index);
				elapsed = 0;
			}

			//If Temp List is empty but P is not yet empty. 
			if(temp.isEmpty() == true && p.isEmpty() == false && currentTime < p.get(0).getArrivalTime())
			{
				currentTime = p.get(0).getArrivalTime();
				temp.add(p.get(0));
				p.remove(0);
			}

			// If Temp is not Empty
			// set curProcess to the process number of the first process 
			// in temp for the next loop
			if(temp.isEmpty() != true)
			{
				curProcess = temp.get(0).getProcessNumber();
			}

			output += " ";
		}
		return output;
	}

	// P (Priority)
	public static String P(List<Process> p)
	{
		// has a Temporary list.
		List<Process> temp = new ArrayList<Process>();
		// Sorts the processes by Arrival Time first, then Priority
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessPriorityComparator()));
		String output = "";

		temp.add(p.get(0)); // adds the first process from P to Temp.
		p.remove(0); //remove the first process from P

		int currentTime = temp.get(0).getArrivalTime(); 
		int elapsed = 0;
		int newCurrentTime = 0;
		int curProcess= temp.get(0).getProcessNumber();
		boolean newEntry = false;
		int startTime = 0;
		int strTime = 0;
		int strtProcNum = 0;
		int prevProcess = -1;

		// run until Temp has elements
		while(temp.isEmpty()!=true)
		{
			output += currentTime; // start
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";

			// if P has elements
			if(p.size() >0)
			{
				//check which is the next time interval
				// get the minimum of (Current time plus remaining time of Temp.get(0) ) 
				//and the arrival time of the first entry of P
				newCurrentTime = Math.min(currentTime + temp.get(0).getRemainingTime(), p.get(0).getArrivalTime());
			}

			// if P has no elements
			else
			{
				// update new current time by adding the remaining time of P to it
				newCurrentTime += temp.get(0).getRemainingTime();
			}

			// time is difference of new current time and current time
			int time = newCurrentTime - currentTime;
			currentTime = newCurrentTime; // set current time to new current time
			elapsed += time;
			temp.get(0).runUntil(time); // run the first Process in Temp for time
			
			prevProcess = curProcess; // previous process = current process (for the next loop)

			// go through P backwards. if the processes' arrival time is less than or equal to current time.
			// add to Temp. Remove the process from P.
			for (int i = p.size()-1; i >= 0; i--){
				if ( p.get(i).getArrivalTime() <= currentTime)
				{
				   	temp.add(p.get(i));
				    p.remove(i);
				}
			}
		
			// since we go went through P backwards. we have to sort the list Temp
			// by Process Priority, then then Arrival Time
			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessPriorityComparator(),new ProcessArrivalTimeComparator()));

			int index = 0;

			//since we sorted, the order may be different
			// find the index of the first process of Temp pre-sorting
			for(int i = 0; i < temp.size(); i++)
			{
				if(temp.get(i).getProcessNumber() == curProcess)
				{
					index =i;
				}
			}

			output += time;

			// If the first process of Temp pre-sorting has a remaining time of less than or equal to 0
			// remove the process from Temp
			if(temp.get(index).getRemainingTime() <= 0)
			{
				output += "X";
				temp.remove(index);
				elapsed = 0;
			}

			//If Temp List is empty but P is not yet empty. 
			if(temp.isEmpty() == true && p.isEmpty() == false && currentTime < p.get(0).getArrivalTime())
			{
				currentTime = p.get(0).getArrivalTime();
				temp.add(p.get(0));
				p.remove(0);
			}

			// If Temp is not Empty
			// set curProcess to the process number of the first process 
			// in temp for the next loop
			if(temp.isEmpty() != true)
			{
				curProcess = temp.get(0).getProcessNumber();
			}
			output += " ";
		}
		return output;
	}

	// Round Robin
	public static String RR(List<Process> p, int q)
	{
		// Sorts the processes by Arrival Time first, then Priority Time
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(), new ProcessPriorityComparator()));
		Queue queue = new LinkedList(); //
		int queueSize = 0; // size tracker of the queue
		String output = "";

		// current time gets the arrival time of the first process in P
		int currentTime = p.get(0).getArrivalTime(); 
		queue.add(p.remove(0));
		queueSize++;
		Process currentProcess;
		int time =0;

		while(p.size() > 0 || queueSize > 0)
		{
			// checks first if there is a new arrival
			if(p.size() > 0 && p.get(0).getArrivalTime() <= currentTime)
			{
				output += currentTime; // start
				output += " ";
				output += p.get(0).getProcessNumber();
				output += " ";

				// if the remaining time of first process in P
				// is less than or equal to the Quantum slice
				if(p.get(0).getRemainingTime()<=q)
				{
					time = p.get(0).getRemainingTime(); // time gets the remaining time of first process
					currentTime+= time; // current time is updated by adding time to it
				}

				// if the remaining time of first process in P
				// is greater than the Quantum slice
				else
				{
					time = q; // time gets the quantum slice
					currentTime += time; // current time is updated by adding time to it
				}

				output += time;
				p.get(0).runUntil(time); // Process is run until the time

				// if process is done, remove from list
				if(p.get(0).getRemainingTime() <= 0)
				{
					output += "X";
					p.remove(0);
				}

				// else, remove from list and add to queue
				else
				{
					queue.add(p.remove(0));
					queueSize++;
				}
			}

			// queue
			// if there is no new arrival. do this
			else
			{
				currentProcess = (Process) queue.peek(); // current process gets the values of the first in queue
				output += currentTime; // start
				output += " ";
				output += currentProcess.getProcessNumber();
				output += " ";

				// if the remaining time of first process in Queue
				// is less than or equal to the Quantum slice
				if(currentProcess.getRemainingTime()<=q)
				{
					time = currentProcess.getRemainingTime();
					currentTime += time;
				}

				// if the remaining time of first process in P
				// is greater than the Quantum slice
				else
				{
					time = q;
					currentTime += time;
				}

				output += time;
				currentProcess.runUntil(time); // run the current process in queue for the time

				// if process is done, remove from queue
				if(currentProcess.getRemainingTime() <= 0)
				{
					output += "X";
					queue.remove();
					queueSize--;

				}

				// else, remove from list and add to the end of queue
				else
				{
					queue.add(queue.remove());
				}
			}
			
			//If Temp List is empty but P is not yet empty. 
			if(p.isEmpty() == false && queueSize <=0 && currentTime < p.get(0).getArrivalTime())
			{
				currentTime = p.get(0).getArrivalTime();
			}

			output += " ";
		}
		return output;
	}

	public static String combine(String str, boolean done)
	{
		boolean isDone = done;

		if(isDone == true)
		{
			return str;
		}

		else{
			isDone = true;
			String[] arg = str.split(" ");
			String finalOutput = "";
			boolean hasX = false;

			for(int j = 1; j < arg.length-4; j+=3)
			{
				if(arg[j].equals(arg[j+3]))
				{
					isDone = false;
					int x = 0;
					int y = 0;
					int sum;
			        try
			    	{
			            x = Integer.parseInt(arg[j+1]);
			            y = Integer.parseInt(arg[j+4]);


			        }
			        catch(Exception e)
			        {
			        	hasX = true;
			            String[] tempX = arg[j+1].split("X");
			            String[] tempY = arg[j+4].split("X");

			            x = Integer.parseInt(tempX[0]);
			            y = Integer.parseInt(tempY[0]);
			        }

			        sum = x+y;

			        if(hasX == true)
			        {
			        	String sumX = Integer.toString(sum);
			        	sumX += "X";
			        	arg[j+1] = sumX;
			        	hasX = false;
			        }

			        else
			        {
			        	arg[j+1] = Integer.toString(sum);
			        }
					//arg[j+1] = arg[j+4];
					arg[j+2] = arg[j+3] = arg[j+4] = ""; // empty the row
				}
			}

			for(int j = 0; j < arg.length; j++)
			{
				if(!arg[j].equals(""))
				{
					finalOutput += arg[j];
					finalOutput += " ";
				}
			}

			return combine(finalOutput,isDone);
		}
	}

	public static String toString(String str)
	{
		String[] arg = str.split(" ");
		String finalOutput = "";

		for(int j = 0; j < arg.length; j++)
		{
			if(!arg[j].equals(""))
			{
				finalOutput += arg[j];
				finalOutput += " ";
			}

			if(j % 3 == 2)
			{
				finalOutput += "\n";
			}
		}

		return finalOutput;
	}
}