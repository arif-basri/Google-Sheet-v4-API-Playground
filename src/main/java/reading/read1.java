package reading;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import common.setup;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class read1 {

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
//            System.out.println("Name, Major");
            for (List<Object> row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                // Note: even if the header contains 5 columns,
                // below statement will give java.lang.IndexOutOfBoundsException
                // (not null value yea)
                // if, for that particular row, from 4th cell to the rest
                // of the columns on the right contains blank value
                System.out.printf("%s | %s \n", row.get(0), row.get(4));

                //so need to check first how many columns for each row before trying to print their value
                int columnCount = row.size();
                System.out.printf("%d \n", columnCount);


            }
        }
    }
}
