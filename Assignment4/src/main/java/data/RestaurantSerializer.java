package data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Restaurant;

@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSerializer {

	
	@Getter @Setter
	private String path = "./";
	
	@Getter @Setter
	private String fileName = "menuAndOrders.ser";
	
	/**
	 * Serializes the given Restaurant, storing its menu and current
	 * orders into a file at the previously-set path with the previously-set name.
	 * <br>
	 * The path defaults to the executable's location and the name
	 * defaults to "menuAndOrders.ser" unless otherwise specified
	 * @param subject - the Restaurant to serialize
	 */
	public void serialize(Restaurant subject) {
		try(FileOutputStream fout = new FileOutputStream(path + fileName);
			ObjectOutputStream out = new ObjectOutputStream(fout)) {
			
			out.writeObject(subject);
		} catch (IOException e) {
			System.out.println("An unexpected error occurred while serializing " + subject.toString() + ".");
			System.out.println("Please consult the stack trace for details.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Deserializes a Restaurant, restoring its menu and current orders from
	 * a file at the previously-set path with the previously-set name.
	 * <br>
	 * The path defaults to the executable's location and the name
	 * defaults to "menuAndOrders.ser" unless otherwise specified
	 * @return A Restaurant object restored from the file if successful,
	 * 		   otherwise null.
	 */
	public Restaurant deserialize() {
		Restaurant result = null;
		
		try (FileInputStream fin = new FileInputStream(path + fileName);
			 ObjectInputStream in = new ObjectInputStream(fin)) {
			
			result = (Restaurant) in.readObject();
		} catch (IOException e) {
			System.out.println("An unexpected error occured while deserializing the Restaurant.");
			System.out.println("Please consult the stack trace for details.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("The MenuItem class was not found while deserializing the Restaurant");
			System.out.println("Please consult the stack trace for details.");
			e.printStackTrace();
		}
		
		return result;
	}
}
