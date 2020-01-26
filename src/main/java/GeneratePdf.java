import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.StringTokenizer;

import com.itextpdf.layout.property.UnitValue;


public class GeneratePdf {

    public static final String DEST = "./results/hello_world.pdf";
    public static void main(String[] args) throws IOException {

        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new GeneratePdf().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {
        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        //Add paragraph to the document
        addParagraph(document, font);

        // Add ListItem
        AddList(document, font);

        // Add image
        addImage(document);

        addTable(document, font, bold);

        //Close document
        document.close();
    }

    private void addTable(Document document, PdfFont font, PdfFont bold) throws IOException {
        Table table = new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
        table.setWidth(UnitValue.createPercentValue(100));
        BufferedReader br = new BufferedReader(new FileReader("data/states.csv"));
        String line = br.readLine();
        process(table, line, bold, true);
        while ((line = br.readLine()) != null) {
            process(table, line, font, false);
        }
        br.close();

        document.add(table);
    }

    private void addImage(Document document) throws MalformedURLException {
        Image image1 = new Image(ImageDataFactory.create("images/googleimage.png"));
        Image image2 = new Image(ImageDataFactory.create("images/googleimage2.png"));
        document.add(image1);
        document.add(image2);
    }

    private void AddList(Document document, PdfFont font) {
        List list = new List()
                .setSymbolIndent(12)
                .setListSymbol("\u2022")
                .setFont(font);

        list.add(new ListItem("Never gonna give you up"))
                .add(new ListItem("Never gonna let you down"))
                .add(new ListItem("Never gonna run around and desert you"))
                .add(new ListItem("Never gonna make you cry"))
                .add(new ListItem("Never gonna say goodbye"))
                .add(new ListItem("Never gonna tell a lie and hurt you"));

        document.add(list);
    }

    private void addParagraph(Document document, PdfFont font) {
        document.add(new Paragraph("Hello World!"));
        document.add(new Paragraph("The quick brown fox jumps over the lazy dog!")).setFont(font);
    }

    public void process(Table table, String line, PdfFont font, boolean isHeader) {
        StringTokenizer tokenizer = new StringTokenizer(line, ";");

        while (tokenizer.hasMoreTokens()) {
            if (isHeader) {
                table.addHeaderCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            } else {
                table.addCell(
                        new Cell().add(
                                new Paragraph(tokenizer.nextToken()).setFont(font)));
            }
        }
    }
}
