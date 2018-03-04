import java.util.*;

public class Testschedule {
	public static void main(String[] args) {
		List readyList =(List) new ArrayList();
		PCB p1 = new PCB(0, 5, Math.random()*100 + 1);
		PCB p2 = new PCB(1, 6, Math.random()*100 + 1);
		PCB p3 = new PCB(2, 7, Math.random()*100 + 1);
		readyList.add(p1);
		readyList.add(p2);
		readyList.add(p3);
		Schedule s = new Schedule(readyList);
		while(!s.getReadyList().isEmpty())
			s.priorityFirst();
		System.out.println("All PROCESS has completed");
	}	
}
//PCB类
class PCB {
	private int id = -1;
	private String status = "ready";
	private double ProcessNeedTime;    //进程运行所需时间
	private int priority = 5;  //优先级数
	
	public PCB() {         
		}
	
	public PCB(int id, int priority, double ProcessNeedTime) {
		this.id = id;
		this.priority = priority;
		this.ProcessNeedTime = ProcessNeedTime;
	}

	public void setStatus(String status) {        //修改进程状态
		this.status = status;	
	}
	
	public String getStatus() {                   
		return status;	
	}
	
	public int getId() {
		return id;	
	}
	
	public void setProcessNeedTime(double ProcessNeedTime) {
		this.ProcessNeedTime = ProcessNeedTime;	
	}
	
	public double getProcessNeedTime() {
		return ProcessNeedTime;	
	}
	
	public void setPriority(int priority) {
		this.priority = priority;	
	}
	 
	public int getPriority() {
		return priority;	
	}
	
	public void clearPcb() {           
		id = -1;
		priority = 5;
		status = "ready";
		ProcessNeedTime = 0;		
	}
	
	public String toString() {
		return "           ProcessInformation：   \n************************\n   ProcessID: " + this.id + "\nPRIORITY:  " + this.priority + "\nSTATUS: " + this.status + "\nProcess_NEED_TIME: " + this.ProcessNeedTime  ;
	}
}



class WaitIO implements Runnable {             
	private List blockList, readyList;
	
	public WaitIO(List blockList, List readyList) {
		this.blockList = blockList;	
		this.readyList = readyList;
	} 
	public void run() {
		try{
			Thread.sleep(2000);
		} catch(InterruptedException e) {
				e.printStackTrace();
			}
		PCB p = (PCB)blockList.remove(0);
		readyList.add(p);
		System.out.printf("^^^^^^^^^^^^^^^^^^\n %d Process has completed the IO operation,back to the readyList\n：", p.getId());
	}	
}

class Schedule {             //进程调度类
	private int i1 = 0, i2 = 0, i3 = 0, i4 = 0;
	private PCB pcb;
	int num;
	public Schedule() {
		}
	List readyList;
	public Schedule(List readyList) {
		this.readyList = readyList;
	}
	
	List <PCB> runingList = (List) new ArrayList();
	List <PCB> blockList = (List) new ArrayList();
	List <PCB> exitList = (List) new ArrayList();
	
	public List getReadyList() {
		return readyList;	
	}
	public void priorityFirst() {

		PCB pcbMaxpriority = (PCB)readyList.get(0);
		
	  readyList.remove(pcbMaxpriority);
		runingList.add(pcbMaxpriority);
		pcbMaxpriority.setStatus("running");
		System.out.println("PROCESS" + pcbMaxpriority.getId() + "Is Running");
		System.out.println(pcbMaxpriority.toString());
		
		System.out.println("            The information of allList");
		System.out.println("************************************************");
		System.out.println("The information of readyList:  \n");
	  while(i1<readyList.size()) System.out.println("The Number"+((PCB)readyList.get(i1++)).getId() + " PROCESS  ");
		System.out.println("The information of runingList:   \n");
	  if(i2<runingList.size()) System.out.println("The Number"+((PCB)runingList.get(i2++)).getId() + "PROCESS  ");
		System.out.println("The information of blockList:    \n");
		while(i3<blockList.size()) System.out.println("The Number"+((PCB)blockList.get(i3++)).getId() + "PROCESS  ");
		System.out.println("The information of exitList:    \n");
		while(i4<exitList.size()) System.out.println("The Number"+((PCB)exitList.get(i4++)).getId() + "PROCESS  ");
	  
		System.out.printf("Please select the status：\n************************************\n0: Enter exit Queue\n1: Enter block Queue\n2 : Enter ready Queue\n");
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		switch(choice)  {
			case 0: {runingList.remove(pcbMaxpriority); exitList.add(pcbMaxpriority); pcbMaxpriority.setStatus("exit"); pcbMaxpriority.clearPcb();
				       System.out.println("The Process has been operated");break;}
			case 1: {runingList.remove(pcbMaxpriority); blockList.add(pcbMaxpriority); pcbMaxpriority.setStatus("blocked");
				       System.out.println("The Process needs IO ,Enter the blockQueue");break;}
			case 2: {runingList.remove(pcbMaxpriority); readyList.add(pcbMaxpriority); pcbMaxpriority.setStatus("ready");
				       System.out.println("The time slice out，PROCESS is no complete，Enter the readyList");break;}	
		}
		if(choice == 1) {
			WaitIO wi =new WaitIO(blockList, readyList);
			Thread th = new Thread(wi);
			th.start();
		}
	}

}