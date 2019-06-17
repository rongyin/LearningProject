/**
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author frankyin
 *
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	@Override
	public Supplier<List<T>> supplier() {
		
		Supplier<List<T>> s= () ->{
			System.out.println("suppler...");
			List<T> list = new ArrayList<T>();
			Room r = new Room("table",45);
			
			//list.add((T) r);
			return list;
		};
		return s;
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		BiConsumer<List<T>, T> bc = (list, t)->{
			System.out.println("accumulator...");
			list.add(t);
		};
		return bc;
	}

	@Override
	public BinaryOperator<List<T>> combiner() {
		System.out.println("combiner start...");
		BinaryOperator<List<T>> bo = (list1,list2)->{
			System.out.println("combiner...");
			 list1.addAll(list2);
			 return list1;
		};
		return bo;
	}

	@Override
	public Function<List<T>, List<T>> finisher() {
		System.out.println("finisher...");
		// TODO Auto-generated method stub
		//return Function.identity();
		return (list1) -> {
			Room r = new Room("final",22);
		 list1.add((T)r);
		 return list1;
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		System.out.println("characteristics...");
		// TODO Auto-generated method stub
		return Collections.unmodifiableSet(EnumSet.of(
				Characteristics.UNORDERED));
	}

}
