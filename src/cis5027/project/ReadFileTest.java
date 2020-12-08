package cis5027.project;

public class ReadFileTest {

	public ReadFileTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		// TODO overload SensorData's readCsv with string types 
		//      (so we can switch between args and specified values)
		
		String fileLocation = "inputs/sensor_data.csv";
		int delay = 0;
		
		// if we want to pass file location and delay as arguments
		
		// String fileLocation = args[0];
		// int delay = Integer.parseInt(args[1]);
		
		String split = ",";
		
		
		// initialise csv reader object
		CsvReader csvReader = new CsvReader(fileLocation, split, delay);
		
		// initialise sensor data object with data from csv
		SensorData sensorData = csvReader.readCsv();
		
		// Begin test
			System.out.println("Light levels:");
			
			for(double lightLevel: sensorData.getLightLevels().subList(0, 10)) {
				System.out.println(lightLevel);
			}
			
			System.out.println("Temperatures:");
			
			for(double temperature: sensorData.getTemperatures().subList(0, 10)) {
				System.out.println(temperature);
			}
		// end test
		
	}

}
