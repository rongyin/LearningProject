import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class HashSetCollector<String> implements Collector<String, HashMap<String,String>, HashMap<String,String>> {

	@Override
	public Supplier<HashMap<String, String>> supplier() {
		// TODO Auto-generated method stub
		return HashMap::new;
	}

	@Override
	public BiConsumer<HashMap<String, String>, String> accumulator() {
		// TODO Auto-generated method stub
		return (map , v) ->{
			map.put((String) "f", v);
		};
	}

	@Override
	public BinaryOperator<HashMap<String, String>> combiner() {
		// TODO Auto-generated method stub
		BinaryOperator<HashMap<String, String>> no = (map1,map2) ->{
			System.out.println("combiner");
	 map1.putAll(map2);
			return map1;
		};
		return no;
	}

	@Override
	public Function<HashMap<String, String>, HashMap<String, String>> finisher() {
		// TODO Auto-generated method stub
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		// TODO Auto-generated method stub
		return Collections.unmodifiableSet(EnumSet.of(
				Characteristics.UNORDERED));
	}





}
