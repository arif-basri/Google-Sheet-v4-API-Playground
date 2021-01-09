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

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final String spreadsheetId = "1LjCBvNAxCm4d9sUbi8ssjc1-Mi5MismbamebjYWqE08"; //PHSP OP
        //final String spreadsheetId = "1PpX11ooHQwk9BYCuRo6HfH295vf9OfupGWMivthm5uQ""; //PHLM IP
        final String range = "Sheet1";

        Credential credential = setup.getCredentials();
        Sheets service = setup.getSheetsService(credential);

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            int rowIdx = 0;
            for (List<Object> row : values) {
                rowIdx++;
                System.out.println("Reading Row: "+rowIdx+" out of : "+values.size());
                int columnCount = row.size();
//                int columnCount =7;  //<-- you will get indexOutOfBoundException if fixed the column count to read
                Object valueCol=null;
                ArrayList<String> valueLn = new ArrayList<String>();

                for (int col = 0; col < columnCount-1; col++) {
                    System.out.println("Reading columns: "+(col+1)+" out of : "+columnCount);

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
                }

                System.out.println(valueLn);

            }
        }
    }
}
