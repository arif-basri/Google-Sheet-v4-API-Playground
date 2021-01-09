package reading;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import shared.setup;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

            for (List<Object> row : values) {
                int columnCount = row.size();
                Object value=null;

                for (int col = 0; col < columnCount-1; col++) {
                    if(row.get(col)!=null){
                        System.out.println("getting value" + col);
                        value = row.get(col);//.toString();
                        System.out.println("got value "+ col);
                    }

                    if (value == null||value.toString().isEmpty()){
                        System.out.println("null value");
                    }
                    else {
                        System.out.println("value : "+value.toString());
                    }

                }


            }
        }
    }
}
