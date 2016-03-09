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
			ArrayList<Process> process = new ArrayList<Process>();
			String input = sc.nextLine();
			String[] arg = input.split(" ");

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
					SJF();
					break;
				}
				case "SRTF":
				{
					SRTF();
					break;
				}
				case "P":
				{
					P();
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
	// First Come First Served
	public static String FCFS(ArrayList<Process> p)
	{
		Collections.sort(p); // sort by their arrival time;
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

			p.get(0).yourTurn(p.get(0).getBurstTime());

			if(p.get(0).getRemainingTime() == 0)
			{
				output += "X\n";
				p.remove(0);
			}
		}
		return output;
	}

	//Shortest Job First, non­preemptive
	public static void SJF()
	{
		System.out.println("SJF");
	}

	//SRTF (Shortest Remaining Time First, SJF preemptive), 
	public static void SRTF()
	{
		System.out.println("SRTF");
	}

	// P (Priority), 
	public static void P()
	{
		System.out.println("P");
	}

	// Round Robin
	public static String RR(ArrayList<Process> p, int time)
	{
		Collections.sort(p); // sort by their arrival time;
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

				p.get(counter).yourTurn(time);


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
}