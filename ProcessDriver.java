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
			System.out.println(i);
			
			String input = sc.nextLine();
			String[] arg = input.split(" ");

			List<Process> process = new ArrayList<Process>();
			for(int j = 1; j <= Integer.parseInt(arg[0]); j++)
			{
				String inputProcess = sc.nextLine();
				String[] arg1 = inputProcess.split(" ");
				int a = Integer.parseInt(arg1[0]);
				int b = Integer.parseInt(arg1[1]);
				int p = Integer.parseInt(arg1[2]);

				System.out.println(arg[1] + " " + a + " " + b + " " + p);

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
					String fin =  SRTF(process);
					String what = format(fin);
					System.out.print(what);

					break;
				}
				case "P":
				{
					String fin =  P(process);
					String what = format(fin);
					System.out.print(what);
					break;
				}
				case "RR":
				{
					System.out.print(RR(process,Integer.parseInt(arg[2])));
					break;
				}
			}

		}
	}

	// First Come First Served
	public static String FCFS(List<Process> p)
	{
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator()));
		String output = "";
		int currentTime = 0; // 0 ns

		while(p.isEmpty()!=true)
		{
			if(p.get(0).getArrivalTime() <= currentTime)
			{
				output += currentTime;
			}

			else
			{
				output += p.get(0).getArrivalTime();
			}
			output += " ";
			output += p.get(0).getProcessNumber();
			output += " ";
			output += p.get(0).getBurstTime();

			currentTime += p.get(0).getBurstTime();

			p.get(0).runUntil(p.get(0).getBurstTime());

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
		List<Process> temp = new ArrayList<Process>();
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessRemainingTimeComparator()));
		String output = "";
		int currentTime = p.get(0).getArrivalTime(); // 

		temp.add(p.get(0));
		p.remove(0);

		while(temp.isEmpty()!=true)
		{
			output += currentTime; // start
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";
			output += temp.get(0).getRemainingTime();

			currentTime += temp.get(0).getRemainingTime();
			temp.get(0).runUntil(temp.get(0).getRemainingTime());

			if(temp.get(0).getRemainingTime() <= 0)
			{
				output += "X\n";
				temp.remove(0);

				if(temp.isEmpty() != true)
				{
					if(currentTime < temp.get(0).getArrivalTime())
					{
						currentTime = temp.get(0).getArrivalTime();
					}
				}

				if(temp.isEmpty() == true && p.isEmpty() != true)
				{
					if(currentTime < p.get(0).getArrivalTime())
					{
						currentTime = p.get(0).getArrivalTime();
					}
				}
			}

			// 
			for (int i = p.size()-1; i >= 0; i--){
			    if ( p.get(i).getArrivalTime() <= currentTime)
			    {
			    	temp.add(p.get(i));
			        p.remove(i);
			    }
			 }

			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessRemainingTimeComparator()));
		}
		return output;
	}

	//SRTF (Shortest Remaining Time First, SJF preemptive), 
	public static String SRTF(List<Process> p)
	{
		List<Process> temp = new ArrayList<Process>();
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessRemainingTimeComparator()));
		String output = "";

		temp.add(p.get(0));
		p.remove(0);

		int currentTime = temp.get(0).getArrivalTime(); // 
		int elapsed = 0;
		int newCurrentTime = 0;
		int curProcess= temp.get(0).getProcessNumber();
		boolean newEntry = false;
		int startTime = 0;
		int strTime = 0;
		int strtProcNum = 0;
		int prevProcess = -1;

		while(temp.isEmpty()!=true)
		{
			//System.out.println(curProcess + " " + prevProcess);
			output += currentTime; // start
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";

			if(p.size() >0)
			{
				//check
				newCurrentTime = Math.min(currentTime + temp.get(0).getRemainingTime(), p.get(0).getArrivalTime());

			}

			else
			{
				newCurrentTime += temp.get(0).getRemainingTime();
			}

			int time = newCurrentTime - currentTime;
			currentTime = newCurrentTime;
			elapsed += time;
			temp.get(0).runUntil(time);
			
			prevProcess = curProcess;

			for (int i = p.size()-1; i >= 0; i--){
				if ( p.get(i).getArrivalTime() <= currentTime)
				{
				   	temp.add(p.get(i));
				    p.remove(i);
				}
			}
		
			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessRemainingTimeComparator()));

			int index = 0;

			for(int i = 0; i < temp.size(); i++)
			{
				if(temp.get(i).getProcessNumber() == curProcess)
				{
					index =i;
				}
			}

			if(curProcess != temp.get(0).getProcessNumber())
			{
				newEntry = true;
				output += elapsed;
				elapsed =0;
			}
			
			else
			{
				
				newEntry = false;
				output += time;
			}


			if(temp.get(index).getRemainingTime() <= 0)
			{
				output += "X";
				temp.remove(index);
				elapsed = 0;
			}

			if(temp.isEmpty() != true)
			{
				curProcess = temp.get(0).getProcessNumber();
			}
			//output +="\n";
			output += " ";
		}
		return output;
	}

	// P (Priority), 
	public static String P(List<Process> p)
	{
		List<Process> temp = new ArrayList<Process>();
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator(),
	                new ProcessPriorityComparator()));
		String output = "";

		temp.add(p.get(0));
		p.remove(0);

		int currentTime = temp.get(0).getArrivalTime(); // 
		int elapsed = 0;
		int newCurrentTime = 0;
		int curProcess= temp.get(0).getProcessNumber();
		boolean newEntry = false;
		int startTime = 0;
		int strTime = 0;
		int strtProcNum = 0;
		int prevProcess = -1;

		while(temp.isEmpty()!=true)
		{
			//System.out.println(curProcess + " " + prevProcess);
			output += currentTime; // start
			output += " ";
			output += temp.get(0).getProcessNumber();
			output += " ";

			if(p.size() >0)
			{
				//check
				newCurrentTime = Math.min(currentTime + temp.get(0).getRemainingTime(), p.get(0).getArrivalTime());

			}

			else
			{
				newCurrentTime += temp.get(0).getRemainingTime();
			}

			int time = newCurrentTime - currentTime;
			currentTime = newCurrentTime;
			elapsed += time;
			temp.get(0).runUntil(time);
			
			prevProcess = curProcess;

			for (int i = p.size()-1; i >= 0; i--){
				if ( p.get(i).getArrivalTime() <= currentTime)
				{
				   	temp.add(p.get(i));
				    p.remove(i);
				}
			}
		
			Collections.sort(temp, new ProcessChainedComparator(
	            new ProcessPriorityComparator()));

			/*or(Process ct: temp)
			{
				System.out.println(currentTime + " " + ct.getProcessNumber() + " " + ct.getPriority());
			}*/

			int index = 0;

			for(int i = 0; i < temp.size(); i++)
			{
				if(temp.get(i).getProcessNumber() == curProcess)
				{
					index =i;
				}
			}

			if(curProcess != temp.get(0).getProcessNumber())
			{
				newEntry = true;
				output += elapsed;
				elapsed =0;
			}
			
			else
			{
				
				newEntry = false;
				output += time;
			}


			if(temp.get(index).getRemainingTime() <= 0)
			{
				output += "X";
				temp.remove(index);
				elapsed = 0;
			}

			if(temp.isEmpty() != true)
			{
				curProcess = temp.get(0).getProcessNumber();
			}
			//output +="\n";
			output += " ";
		}
		return output;
	}

	// Round Robin
	public static String RR(List<Process> p, int time)
	{
		Collections.sort(p, new ProcessChainedComparator(
	                new ProcessArrivalTimeComparator()));
		String output = "";
		int currentTime = p.get(0).getArrivalTime(); // 0 ns
		int counter = 0;

		while(p.isEmpty()!=true)
		{
			int lastIndex = p.size()-1;

			if(counter > lastIndex || currentTime < p.get(counter).getArrivalTime())
			{
				//System.out.println("HI" +counter);
				counter = 0;
			}

			else
			{
				output += currentTime;
				output += " ";
				output += p.get(counter).getProcessNumber();
				output += " ";
				if(p.get(counter).getRemainingTime() < time)
				{
					output += p.get(counter).getRemainingTime();
					currentTime += p.get(counter).getRemainingTime();
				}

				else
				{
					output += time;
					currentTime += time;
				}

				p.get(counter).runUntil(time);


				if(p.get(counter).getRemainingTime() <= 0)
				{
					if(counter+1 <= lastIndex)
					{
						if(currentTime < p.get(counter+1).getArrivalTime())
						{
							currentTime = p.get(counter+1).getArrivalTime();
						}
					}
					output += "X";
					p.remove(counter);

				}

				output +="\n";
				counter++;
			}
		}
		return output;
	}

	public static String format(String str)
	{
		String[] arg = str.split(" ");
		//System.out.println(arg.length);
		String finalOutput = "";

		for(int j = 1; j < arg.length-4; j+=3)
		{
			//System.out.println(arg[j] + " " + arg[j+3]);
			if(arg[j].equals(arg[j+3]))
			{
				//System.out.println("WHAT");
				arg[j+1] = arg[j+4];
				arg[j+2] = arg[j+3] = arg[j+4] = ""; 
			}
		}

		for(int j = 0; j < arg.length; j++)
		{
			if(!arg[j].equals(""))
			{
				finalOutput += arg[j];
				finalOutput += " ";
			}

			if(!arg[j].equals("") && j % 3 == 2)
			{
				finalOutput += "\n";
			}

		}


		return finalOutput;
	}
}