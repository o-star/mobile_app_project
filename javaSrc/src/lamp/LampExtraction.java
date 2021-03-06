package lamp;

import map.Request;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class LampExtraction {

    public static double latUpperBound;
    public static double latLowerBound;
    public static double lonLeftBound;
    public static double lonRightBound;
    public static ArrayList<Request.Coord> road_lamp = new ArrayList<Request.Coord>();
    public static ArrayList<Request.Coord> street_lamp = new ArrayList<Request.Coord>();

    public static ArrayList<ArrayList<Request.Coord>> getBoundCoord(double startX, double startY, double endX, double endY) throws IOException {
        double x_margin = 0.0016; // 경도 가장자리 여분
        double y_margin = 0.007; // 위도 가장자리 여분
        double lat_low = 0, lat_high = 0, lon_left = 0, lon_right = 0;
        if (startY >= endY) //위도
        {
            lon_left = endY;
            lon_right = startY;
        }
        else
        {
            lon_left = startY;
            lon_right = endY;
        }
        if (startX >= endX) // # 경도
        {
            lat_low = endX;
            lat_high = startX;
        }
        else {
            lat_low = startX;
            lat_high = endX;
        }

        latUpperBound = lat_high + x_margin; // # 위도 최대값
        latLowerBound = lat_low - x_margin; //  # 위도 최소값
        lonLeftBound = lon_left - y_margin; //  # 경도 최소값
        lonRightBound = lon_right + y_margin; //  # 경도 최대값

        ArrayList<Request.Coord> allRoadLamp = new ArrayList<Request.Coord>();
        ArrayList<Request.Coord> allStreetLamp = new ArrayList<Request.Coord>();

        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구시설공단_가로등위치정보_20180514.csv", 1);
        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구광역시_동구_보안등정보_20190614.csv", 2);
        //readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\대구광역시_서구_보안등정보_20190611.csv", 2);
        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구광역시_남구_보안등정보_20190906.csv", 2);
        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구광역시_북구_보안등정보_20190109.csv", 2);
        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구광역시_달서구_보안등정보_20190918.csv", 2);
        //readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\대구광역시_달성군_보안등정보_20190625.csv", 2);
        //readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\대구광역시_수성구_보안등정보_20190924.csv", 2);
        readDataFromCsv("C:\\Users\\HyunSU\\Desktop\\2020\\2020 하반기\\코드페어\\src\\lamp\\information\\대구광역시_중구_보안등정보_20190610.csv", 2);
//        for (int i = 0; i < road_lamp.size(); i++) {
//            System.out.println(road_lamp.get(i).first() + " " + road_lamp.get(i).second());
//        }
        for (int i = 0; i < road_lamp.size(); i++) {
            String lat = road_lamp.get(i).first();
            String lon = road_lamp.get(i).second();
            if(check_boundary(lat, lon))
                allRoadLamp.add(new Request.Coord(lat, lon));
        }
        for (int i = 0; i < street_lamp.size(); i++) {
            String lat = street_lamp.get(i).first();
            String lon = street_lamp.get(i).second();
            if(check_boundary(lat, lon))
                allStreetLamp.add(new Request.Coord(lat, lon));
        }
        //System.out.println("DD");
        ArrayList<ArrayList<Request.Coord>> result = new ArrayList<ArrayList<Request.Coord>>();
        result.add(allRoadLamp);
        result.add(allStreetLamp);
        return result;
    }
    public static boolean check_boundary(String lamp_lat, String lamp_lon){

        double latitude, longitude;
        for (int i = 0; i < lamp_lat.length(); i++) {
            if(lamp_lat.charAt(i) != '.' && Character.isDigit(lamp_lat.charAt(i)) == false) return false;
        }
        for (int i = 0; i < lamp_lon.length(); i++) {
            if(lamp_lon.charAt(i) != '.' && Character.isDigit(lamp_lon.charAt(i)) == false) return false;
        }
        latitude = Double.parseDouble(lamp_lat);
        longitude = Double.parseDouble(lamp_lon);
        if ((latitude >= latLowerBound && latitude <= latUpperBound) && (longitude >= lonLeftBound && longitude <= lonRightBound))
            return true;
        else return false;
    }

    // mark : 1 -> road_lamp, 2 -> street_lamp
    public static void readDataFromCsv(String filePath, int mark) throws IOException {
        String line = null;
        File locationFile = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(locationFile));
            Charset.forName("UTF-8");

            while((line = br.readLine()) != null) {
                String[] token = line.split(",");
                if(mark == 1) road_lamp.add(new Request.Coord(token[2], token[3]));
                else street_lamp.add(new Request.Coord(token[4], token[5]));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
