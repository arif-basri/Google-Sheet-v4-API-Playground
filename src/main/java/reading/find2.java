package reading;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import common.setup;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

//find row index by cell value
public class find2 {
    private static Sheets service;
    private static String spreadsheetId = "1LjCBvNAxCm4d9sUbi8ssjc1-Mi5MismbamebjYWqE08"; //PHSP OP

    public static void main(String... args) throws IOException, GeneralSecurityException {
        init();

    }

    private static void init() throws IOException,GeneralSecurityException {
        // Build a new authorized API client service.

        final String range = "Sheet1!1:1";

        Credential credential = setup.getCredentials();
        service = setup.getSheetsService(credential);

        int sheetIDToDelete = findHiddenWorksheet("HiddenForFormulaPurposes");
        if (sheetIDToDelete>-1) {
            deleteHiddenWorksheet(sheetIDToDelete);
        }
        createHiddenWorksheet("HiddenForFormulaPurposes");


    }

    private static int findHiddenWorksheet(String sheetTitleToFind) throws IOException {
        Spreadsheet response1= service.spreadsheets().get(spreadsheetId).setIncludeGridData(false).execute();

        //find sheet
        List<Sheet> worksheets = response1.getSheets();
        String[] names = new String[worksheets.size()];
        int selectedSheet = -1;
        int selectedSheetID = -1;
        for (int i = 0; i < worksheets.size(); i++) {
            Sheet sheet = worksheets.get(i);
            names[i] = sheet.getProperties().getTitle();
            if (sheet.getProperties().getTitle().contains(sheetTitleToFind)) {
//                selectedSheet = i;
                selectedSheetID = sheet.getProperties().getSheetId();
                System.out.printf("Found sheet: %s (ID:%d)\n", names[i], selectedSheetID);
            }
        }

//        if(selectedSheetID>-1) means exists
            return selectedSheetID;
    }

    private static void deleteHiddenWorksheet(int sheetIDToDelete) throws IOException {
        Spreadsheet response1= service.spreadsheets().get(spreadsheetId).setIncludeGridData(false).execute();


        DeleteSheetRequest delSheetRequest = new DeleteSheetRequest();
        delSheetRequest.setSheetId(sheetIDToDelete);

        List<Request> requests = new ArrayList<>();

        Request request1 = new Request();

        request1.setDeleteSheet(delSheetRequest);
        requests.add(request1);

        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
        requestBody.setRequests(requests);

        BatchUpdateSpreadsheetResponse requestResponse = service.spreadsheets()
                .batchUpdate(spreadsheetId, requestBody).execute();

        System.out.printf("Deleting sheet ID: %d\n", sheetIDToDelete);
    }

    private static void createHiddenWorksheet(String sheetTitleToCreate) throws IOException {
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        addSheetRequest.setProperties(new SheetProperties().setHidden(Boolean.TRUE)
                .setTitle(sheetTitleToCreate));

        List<Request> requests = new ArrayList<>();

        Request request1 = new Request();
        request1.setAddSheet(addSheetRequest);

        requests.add(request1);

        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
        requestBody.setRequests(requests);

        BatchUpdateSpreadsheetResponse requestResponse = service.spreadsheets()
                .batchUpdate(spreadsheetId, requestBody).execute();

        // now you can execute batchUpdate with your sheetsService and SHEET_ID
        int sourceSheetId = requestResponse.getReplies().get(0).getAddSheet().getProperties()
                .getSheetId();
        String sourceSheetTitle = requestResponse.getReplies().get(0).getAddSheet().getProperties()
                .getTitle();

        System.out.printf("Creating sheet: %s (ID:%d)", sourceSheetTitle,sourceSheetId);
//        data.service.spreadsheets().batchUpdate(spreadsheetID, requestBody).execute();
    }
}
