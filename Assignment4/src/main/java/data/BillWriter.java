package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;

import model.MenuItem;
import model.Order;

public class BillWriter {
	private final String defaultDirectory;
	
	/**
	 * Creates a new BillWriter with a default directory
	 * of "./bills".
	 */
	public BillWriter() {
		defaultDirectory = "./bills";
	}
	
	/**
	 * Creates a new BillWriter with the given
	 * default directory.
	 * @param defaultDir - a default directory that is
	 * 		suggested to the user when selecting where
	 * 		to save a receipt
	 */
	public BillWriter(String defaultDir) {
		defaultDirectory = defaultDir;
	}
	
	public void writeReceipt(Order order, Collection<MenuItem> products) {
		JFileChooser f = new JFileChooser(defaultDirectory);
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        f.showSaveDialog(null);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH-mm  d_MMM_yy");
        String receiptTitle = "Receipt order " + order.getId() + " - "
        					  + dateFormatter.format(order.getDate())
        					  + ".txt";
        
        File receiptFile = new File(f.getSelectedFile(), receiptTitle);
        
        try {
			receiptFile.createNewFile();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile));
			writer.write("Receipt"); writer.newLine();
			writer.write("Order " + order.getId()); writer.newLine();
			writer.write("Date: " +
						 DateTimeFormatter.ofPattern("HH:mm, d MMM yy").format(order.getDate()));
			writer.newLine();
			
			writer.newLine();
			Map<MenuItem, Long> itemAndAmount = products.stream()
													   	.collect(Collectors.groupingBy(
													   			 item -> item,
													   			 Collectors.counting()));
			
			BigDecimal total = ( products.stream()
										 .map(MenuItem::computePrice)
										 .reduce((a, b) -> a.add(b))
										 ).get();
			
			itemAndAmount.forEach((item, amount) -> {
				BigDecimal subtotal = item.computePrice().multiply( new BigDecimal(amount) );
				
				String entryWithDots = amount + " x " + item.getName() +
						   "...............................................................";
				try {
					writer.write(entryWithDots, 0, 40);
					writer.write( subtotal.toString() );
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace(); // Something went wrong at IO; just let it go for now
				}
			});
			
			writer.newLine();
			writer.write("Total: " + total.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace(); // Something went wrong at IO; just let it go for now
		}
	}
}
