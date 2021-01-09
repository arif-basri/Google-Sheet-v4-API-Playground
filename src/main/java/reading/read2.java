package reading;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import shared.setup;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class read2 {

    private static List<List<Object>> values = new ArrayList<List<Object>>();
    public static void main(String... args) throws IOException, GeneralSecurityException {
        init();
    }

    private static void init() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final String spreadsheetId = "1LjCBvNAxCm4d9sUbi8ssjc1-Mi5MismbamebjYWqE08"; //PHSP OP
        //final String spreadsheetId = "1PpX11ooHQwk9BYCuRo6HfH295vf9OfupGWMivthm5uQ""; //PHLM IP
        final String range = "Sheet1";

        Credential credential = setup.getCredentials();
        Sheets service = setup.getSheetsService(credential);

        getValues(spreadsheetId, range, service);
    }

    private static void getValues(String spreadsheetId, String range, Sheets service) throws IOException {
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        values = response.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            processValues();
        }
    }

    private static void processValues() {
        int rowIdx = 0;
        ArrayList<String> headerColumnName = new ArrayList<String>();

        for (List<Object> row : values) {
            rowIdx = processRow(rowIdx, headerColumnName, row);
        }
    }

    private static int processRow(int rowIdx, ArrayList<String> headerColumnName, List<Object> row) {
        rowIdx++;
        System.out.println("Reading Row: "+rowIdx+" out of : "+values.size());
        int columnCount = row.size();
//                int columnCount =7;  //<-- you will get indexOutOfBoundException if fixed the column count to read
        Object valueCol=null;
        ArrayList<String> valueLn = new ArrayList<String>();

        for (int col = 0; col < columnCount; col++) {

            valueCol = processColumn(rowIdx, headerColumnName, row, columnCount, valueCol, valueLn, col);
        }

        System.out.println(valueLn);
        return rowIdx;
    }

    private static Object processColumn(int rowIdx, ArrayList<String> headerColumnName, List<Object> row, int columnCount, Object valueCol, ArrayList<String> valueLn, int col) {
        if (rowIdx==1) {
            headerColumnName.add(row.get(col).toString());
            System.out.println("Storing header column: "+col +" : "+headerColumnName.get(col));
        } else {
            System.out.println("Reading columns: "+(col+1)+" out of : "+columnCount+" Name: "+headerColumnName.get(col));
        }

        if(row.get(col)!=null){
//                        System.out.println("getting value " + col);
            valueCol = row.get(col);//.toString();
//                        System.out.println("got value "+ col);
        }

        if (valueCol == null||valueCol.toString().isEmpty()){
//                        System.out.println("null value");
            valueLn.add(col, "<null>");
        } else {
//                        System.out.println("value : "+valueCol.toString());
            valueLn.add(col, valueCol.toString());
        }
        return valueCol;
    }
}
