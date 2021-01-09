package write;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import common.setup;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class write1 {
    private static Sheets service;

    private static void init() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final String spreadsheetId = "1LjCBvNAxCm4d9sUbi8ssjc1-Mi5MismbamebjYWqE08"; //PHSP OP
        //final String spreadsheetId = "1PpX11ooHQwk9BYCuRo6HfH295vf9OfupGWMivthm5uQ""; //PHLM IP
        final String range = "Sheet1!J2";
        final String valueInputOption = "RAW"; //"USER_ENTERED";
        final String majorDimensions = "COLUMNS";
        final List<List<Object>> updList = new ArrayList<List<Object>>();

        Credential credential = setup.getCredentials();
        service = setup.getSheetsService(credential);

        ArrayList<Object> listOfUUID = new ArrayList<Object>();

        final String uuid = UUID.randomUUID().toString(); //.replace("-", "");

        listOfUUID.add(uuid);
        updList.add(listOfUUID);

        updateValues(spreadsheetId, range, valueInputOption, updList, majorDimensions);
    }



    public static void main(String... args) throws IOException, GeneralSecurityException {

        init();

    }

    public static UpdateValuesResponse updateValues(String spreadsheetId, String range,
                                                    String valueInputOption, List<List<Object>> _values, String majorDimensions)
            throws IOException {
        Sheets service = write1.service;
        // [START sheets_update_values]
        List<List<Object>> values;
//        = Arrays.asList(
//                Arrays.asList(
//                        // Cell values ...
//                )
//                // Additional rows ...
//        );
        // [START_EXCLUDE silent]
        values = _values;
        // [END_EXCLUDE]
        ValueRange body = new ValueRange()
                .setValues(values)
                .setMajorDimension(majorDimensions);
        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, range, body)
                        .setValueInputOption(valueInputOption)
                        .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());
        // [END sheets_update_values]
        return result;
    }
}
