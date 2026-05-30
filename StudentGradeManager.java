import java.util.Scanner;
public class StudentGradeManager{
	private String StudentName[];
	private double StudentGrade[];

		StudentGradeManager(String []StudentName,double []StudentGrade){
			this.StudentName=StudentName;
			this.StudentGrade=StudentGrade;
		}

		String []getStudentName(){
			return StudentName;
		}

		double []getStudentGrade(){
			return StudentGrade;
		}
}

class Main{
	public static void main(String [] args){

		Scanner s = new Scanner(System.in);
		System.out.print("Enter the number of Records :");
		int n=s.nextInt();
		s.nextLine(); // consume newline

		String []Name=new String[n];
		double []Marks=new double[n];
		
		for(int i=0;i<n;i++){
		System.out.print("Name :");
		 Name[i] = s.nextLine();

		System.out.print("Grade :");
		 Marks[i] = s.nextDouble();	

		 s.nextLine(); // consume newline


		 System.out.println("");


         }

        StudentGradeManager s1 = new StudentGradeManager(Name,Marks);

		System.out.println("========= Student Report ==========");
		System.out.printf("%-20s %-10s%n", "Student Name", "Grade");

		String []NAME=s1.getStudentName();
		double []GRADES=s1.getStudentGrade();

		for(int i=0;i<3;i++){
			System.out.printf("%-20s %-10.2f%n",NAME[i],GRADES[i]);
		}
		System.out.println("-----------------------------------");

		double sum=0;
		for(int i=0;i<GRADES.length;i++){
		    sum=sum+GRADES[i];
		}

		System.out.println("Average Score :"+sum/GRADES.length);

		double max=GRADES[0];

		for(int i=0;i<GRADES.length;i++){
			   if(GRADES[i]>max){
			   	max=GRADES[i];
			   }
		}

		System.out.println("Highest Score ="+max);

		double min=GRADES[0];

		for(int i=0;i<GRADES.length;i++){
			   if(GRADES[i]<min){
			   	min=GRADES[i];
			   }
		}

		System.out.println("Lowest Score ="+min);
		System.out.println("===================================");


	}
}