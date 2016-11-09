package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Calendar;

public class KookminCarteParser {
	private static final String address = "http://kmucoop.kookmin.ac.kr/restaurant/restaurant.php?w=";
	private static int i;
	
	public static void main(String[] args) throws Exception {
		String today = findToday();
		bubsikPrint(today);	System.out.println();
		haksikPrint(today);	System.out.println();
		facultyPrint(today);System.out.println();
		chunghyangPrint(today);
		System.out.print("\n종료하려면 아무키나 누르세요...");
		System.in.read();
	}
	//오늘의 날짜 찾기
	private static String findToday(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH)+1, date = cal.get(Calendar.DATE);
		return year+"년 "+month+"월 "+date+"일";
	}
	//각 식당마다 오늘 메뉴 부분만 뽑아서 Elements 타입으로 리턴
	private static Elements jsoupConnect(String address) throws Exception {
		String tag = "td[bgcolor=#eaffd9]";
		Document doc = Jsoup.connect(address).get();
		return doc.select(tag);
	}
	//법식 출력
	private static void bubsikPrint(String today) throws Exception {
		System.out.println("## 법학관 한울식당 메뉴 ("+today+") ##\n");
		String tmp; i = 0;
		String bubsikMenu[] = { "바로바로1", "바로바로2", "면이랑", "밥이랑 하나", "밥이랑 두울", "石火랑", "石火랑(조식)"};

		Elements menu = jsoupConnect(address+1);
		
		for (Element res : menu) {
			tmp = res.text();
			if (tmp.contains("*중식")) tmp = tmp.replace("*석식*", "\n*석식*");
			bubsikMenu[i] = "------- <"+bubsikMenu[i]+"> -------\n" + tmp+"\n";
			
			if(1<i && i<6) System.out.println(bubsikMenu[i]);
			i++;
		}
	}
	//학식 출력
	private static void haksikPrint(String today) throws Exception {
		System.out.println("## 복지관 학생식당 메뉴 ("+today+") ##\n");
		String haksikMenu[] = { "착한아침", "가마", "누들송(면)", "누들송(카페테리아)", "인터쉐프", "데일리밥"};
		
		for(i=0;i<6;i++) haksikMenu[i] = "------- <"+haksikMenu[i]+"> -------\n"; i=0;
		
		Elements menu = jsoupConnect(address+2);
		
		for (Element res : menu) {
			String tmp = res.text();
			
			if(i==0) tmp = "*조식* "+tmp+"\n";
			else if(i<6) tmp = "*중식* "+tmp+"\n";
			else tmp = "*석식* "+tmp+"\n";
			
			if(i==6) haksikMenu[1]+=tmp;
			else if(i==7) haksikMenu[4]+=tmp;
			else if(i==8){ haksikMenu[5]+=tmp; break; }
			else haksikMenu[i]+=tmp;
			i++;
		}
		
		for(i=0;i<6;i++) System.out.println(haksikMenu[i]);
	}
	//교직원식당 출력
	private static void facultyPrint(String today) throws Exception{
		System.out.println("## 복지관 교직원식당 메뉴 ("+today+") ##\n");
		String facultyMenu[] = {"키친1", "키친2", "주문식", "석식"};
		
		for(i=0;i<4;i++) facultyMenu[i] = "------- <"+facultyMenu[i]+"> -------\n"; i=0;
		
		Elements menu = jsoupConnect(address+3);
		
		for(Element res : menu){
			String tmp = res.text();
			
			if(i>2) tmp = "*석식* "+tmp+"\n";
			else tmp = "*중식* "+tmp+"\n";
			
			System.out.println(facultyMenu[i++]+=tmp);
		}
	}
	//청향 출력
	private static void chunghyangPrint(String today) throws Exception{
		System.out.println("## 법학관 청향 메뉴 ("+today+") ##\n");
		String chunghyangMenu[] = {"메뉴1", "메뉴2", "메뉴3", "메뉴4", "메뉴5", "메뉴6", "메뉴7"};
		
		for(i=0;i<7;i++) chunghyangMenu[i] = "------- <"+chunghyangMenu[i]+"> -------\n"; i=0;
		
		Elements menu = jsoupConnect(address+4);
		
		for(Element res : menu) System.out.println(chunghyangMenu[i++]+"*중식* "+res.text()+"\n");
	}
}
