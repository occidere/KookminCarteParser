package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Calendar;

public class KookminCarteParser {
	private static final String address = "http://kmucoop.kookmin.ac.kr/restaurant/restaurant.php?w=";
	private static String breakfast="", lunch="", dinner="";
	
	public static void main(String[] args) throws Exception {
		String today = findToday();
		System.out.println("## "+today+" 식단표 ##\n");
		parseBubsik();
		parseHaksik();
		parseFaculty();
		parseChunghyang();
		printAll();
		System.out.print("종료하려면 아무키나 누르세요...");
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
	
	//법식
	private static void parseBubsik() throws Exception {
        String bubsik = "[법식]", tmp;
        Elements menu = jsoupConnect(address+1);
        for (Element res : menu) {
            tmp = removeBracket(res.text());
            if(tmp.contains("중식")){
            	if(tmp.contains("석식")){
            		lunch+=bubsik+tmp.substring(tmp.indexOf("식*")+2, tmp.indexOf("*석식*"))+"\n";
            		dinner+=bubsik+tmp.substring(tmp.indexOf("*석식*")+4)+"\n";
            	}
            	else lunch+=bubsik+tmp.substring(tmp.indexOf("식*")+2)+"\n";
            }
            else if(tmp.contains("중석식")){
                lunch+=bubsik+tmp.substring(tmp.indexOf("식*")+2)+"\n";
                dinner+=bubsik+tmp.substring(tmp.indexOf("식*")+2)+"\n";
            }
            else breakfast+=bubsik+" "+tmp+"\n";
        }
        breakfast+="\n"; lunch+="\n"; dinner+="\n";
    }
	
	//학식
    private static void parseHaksik() throws Exception {
        String haksik = "[학식]", tmp;
        Elements menu = jsoupConnect(address+2);
        int i=0;
        for (Element res : menu) {
            tmp = removeBracket(res.text());
            if(i==0) breakfast+=(haksik+" "+tmp+"\n"); //조식
            else if(i<6) lunch+=(haksik+" "+tmp+"\n"); //중식
            else dinner+=(haksik+" "+tmp+"\n"); //석식
            i++;
            if(i>8) break; //학식 중국집 메뉴는 출력 안한다.
        }
        breakfast+="\n"; lunch+="\n"; dinner+="\n";
    }
	
  //교직원식당
    private static void parseFaculty() throws Exception{
        String faculty = "[교직원]", tmp;
        Elements menu = jsoupConnect(address+3);
        int i=0;
        for(Element res : menu){
            tmp = removeBracket(res.text());tmp = tmp.substring(tmp.indexOf(']')+1).trim();
            if(i>2) dinner+=(faculty+" "+tmp+"\n");//석식
            else lunch+=(faculty+" "+tmp+"\n");//중식
            i++;
        }
        lunch+="\n"; dinner+="\n";
    }
	
  //청향
    private static void parseChunghyang() throws Exception{
        String chunghyang = "[청향]", tmp;
        Elements menu = jsoupConnect(address+4);
        //청향은 중식만 운영
        for(Element res : menu){
            tmp = removeBracket(res.text());
            lunch+=(chunghyang+" "+tmp+"\n");
        }
        lunch+="\n";
    }
	
	//파싱한 메뉴 출력
	private static void printAll(){
		System.out.println("------- <조식> -------");
		System.out.println(breakfast);
		System.out.println("------- <중식> -------");
		System.out.println(lunch);
		System.out.println("------- <석식> -------");
		System.out.println(dinner);
	}
	
	//메뉴 앞에 붙은 보기싫은 수식어구 제거
	private static String removeBracket(String menu){ 
		char tmp[] = menu.toCharArray();
		int i, j=0, size = menu.length();
		StringBuilder res = new StringBuilder();
		char stack[] = new char[size]; 
		for(i=0;i<size;i++){
			//() 또는 []내부의 쓸모없는 원산지나 미사여구 제거
			if(tmp[i]=='('){
				while(true) {
					if(tmp[i]==')') break;
					else i++;
				}
			}
			else if(tmp[i]=='[') {
				while(true) {
					if(tmp[i]==']') break;
					else i++;
				}
			}
			//괄호 밖의 평문인 경우 전부 스택에 담는다.
			else stack[j++] = tmp[i];
		}
		for(i=0;i<j;i++) res.append(stack[i]);
		return res.toString().trim();
	}
}
