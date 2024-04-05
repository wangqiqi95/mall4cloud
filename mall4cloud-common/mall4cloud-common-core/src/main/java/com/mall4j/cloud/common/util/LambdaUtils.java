package com.mall4j.cloud.common.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class LambdaUtils {
    
    
    
    public static <T> List<T> filter(List<T> c,Predicate<? super T> predicate){
        return  c.stream().filter(predicate).collect(Collectors.toList());
    }
    
    public static <T> T max(List<T> c,Comparator<? super T> comparator){
        return  c.stream().collect(Collectors.maxBy(comparator)).get();
    }
    
    public static <T> T min(List<T> c,Comparator<? super T> comparator){
        return  c.stream().collect(Collectors.minBy(comparator)).get();
    }
    
    
    @SuppressWarnings("unchecked")
    public static <R extends Number, T> R min(List<T> c, Function<? super T, ?  extends Number> keyExtractor){
        T t=c.stream().collect(Collectors.minBy((c1,c2)->keyExtractor.apply(c1).doubleValue()>keyExtractor.apply(c2).doubleValue()?1:-1)).get();
        return  (R) keyExtractor.apply(t);
    }
    
    @SuppressWarnings("unchecked")
    public static <R extends Number, T> R max(List<T> c, Function<? super T, ?  extends Number> keyExtractor){
        T t=c.stream().collect(Collectors.maxBy((c1,c2)->keyExtractor.apply(c1).doubleValue()>keyExtractor.apply(c2).doubleValue()?1:-1)).get();
        return  (R) keyExtractor.apply(t);
    }
    
    
	public static <R, L> List<R> mapToList(Collection<L> c, Function<? super L, ? extends R> fuc) {
		return c.stream().map(fuc).collect(Collectors.toList());
	}
	public static <R, L> List<R> mapToList(Stream<L> c, Function<? super L, ? extends R> fuc) {
		return c.map(fuc).collect(Collectors.toList());
	}

	public static <T, K, V> Map<K, V> toMap(Collection<T> c, Function<? super T, ? extends K> keyMapper,
										 Function<? super T, ? extends V> valueMapper) {
		return c.stream().collect(Collectors.toMap(keyMapper, valueMapper));
	}
	
	public static <T, K, V> Map<K, V> toMap(Collection<T> c, Function<? super T, ? extends K> keyMapper,
			 Function<? super T, ? extends V> valueMapper,BinaryOperator<V> mergeFunction) {
	return c.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
	}

	public static <T, K, V> Map<K, V> toMap(Stream<T> c, Function<? super T, ? extends K> keyMapper,
											Function<? super T, ? extends V> valueMapper) {
		return c.collect(Collectors.toMap(keyMapper, valueMapper));
	}

	public static <T, K> Map<K, T> toMap(Collection<T> c, Function<? super T, ? extends K> keyMapper) {
		return c.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
	}

	public static <T, K> Map<K, T> toMap(Stream<T> c, Function<? super T, ? extends K> keyMapper) {
		return c.collect(Collectors.toMap(keyMapper, Function.identity()));
	}

	public static <T,K,A,D> Map<K, D> group(Collection<T> c, Function<? super T, ? extends K> classifier,
														Collector<? super T, A, D> downstream) {
		return c.stream().collect(groupingBy(classifier, downstream));
	}

	public static <T,K,A,D> Map<K, D> group(Stream<T> s, Function<? super T, ? extends K> classifier,
											Collector<? super T, A, D> downstream) {
		return s.collect(groupingBy(classifier, downstream));
	}
	
    public static <T, K ,P> Map<K, List<P>> groupList(Collection<T> c, Function<? super T, ? extends K> keyClassifier,Function<? super T, ? extends P> valueClassifier) {
        return  c.stream().collect(groupingBy(keyClassifier, Collectors.mapping(valueClassifier, toList())));
    }
	
	public static <T,K> Map<K, List<T>> groupList(Stream<T> c, Function<? super T, ? extends K> classifier) {
		return group(c, classifier, toList());
	}

	public static <T,K> Map<K, List<T>> groupList(Collection<T> c, Function<? super T, ? extends K> classifier) {
		return group(c.stream(), classifier, toList());
	}

	public static <T,K> Map<K, Integer> groupSumInteger(Collection<T> c, Function<? super T, ? extends K> classifier,
															ToIntFunction<? super T> classifier2) {
		return c.stream().collect(groupingBy(classifier, Collectors.summingInt(classifier2)));
	}

	public static <T,K> Map<K, BigDecimal> groupSumDecimal(Collection<T> c, Function<? super T, ? extends K> classifier,
													  ToBigDecimalFunction<? super T>  downstream) {
		return c.stream().collect(groupingBy(classifier, summingBigDecimal(downstream)));
	}

	public static <T> Collector<List<T>, List<T>, List<T>> addList() {
		return Collector.of(ArrayList::new, List::addAll, (x, y) -> {
			x.addAll(y);
			return x;
		});
	}

	@FunctionalInterface
	public interface ToBigDecimalFunction<T> {
		BigDecimal applyAsBigDecimal(T value);
	}

	@SuppressWarnings("unchecked")
	private static <I, R> Function<I, R> castingIdentity() {
		return i -> (R) i;
	}

	static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
		private final Supplier<A> supplier;
		private final BiConsumer<A, T> accumulator;
		private final BinaryOperator<A> combiner;
		private final Function<A, R> finisher;
		private final Set<Characteristics> characteristics;

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
					  Function<A, R> finisher, Set<Characteristics> characteristics) {
			this.supplier = supplier;
			this.accumulator = accumulator;
			this.combiner = combiner;
			this.finisher = finisher;
			this.characteristics = characteristics;
		}

		CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner,
					  Set<Characteristics> characteristics) {
			this(supplier, accumulator, combiner, castingIdentity(), characteristics);
		}

		@Override
		public BiConsumer<A, T> accumulator() {
			return accumulator;
		}

		@Override
		public Supplier<A> supplier() {
			return supplier;
		}

		@Override
		public BinaryOperator<A> combiner() {
			return combiner;
		}

		@Override
		public Function<A, R> finisher() {
			return finisher;
		}

		@Override
		public Set<Characteristics> characteristics() {
			return characteristics;
		}
	}

	public static <T> BigDecimal sumBigDecimal(Collection<T> c, ToBigDecimalFunction<? super T> mapper) {
		return c.stream().collect(summingBigDecimal(mapper));
	}
	
    public static <T> Integer summingInt(Collection<T> c, ToIntFunction<? super T> mapper) {
        return c.stream().collect(Collectors.summingInt(mapper));
    }
    
    public static <T> Long summingLong(Collection<T> c, ToLongFunction<? super T> mapper) {
        return c.stream().collect(Collectors.summingLong(mapper));
    }
	public static <T> Collector<T, ?, BigDecimal> summingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
		return new CollectorImpl<>(() -> new BigDecimal[1], (a, t) -> {
			if (a[0] == null) {
				a[0] = BigDecimal.ZERO;
			}
			a[0] = a[0].add(mapper.applyAsBigDecimal(t));
		}, (a, b) -> {
			a[0] = a[0].add(b[0]);
			return a;
		}, a -> a[0], Collections.emptySet());
	}
}
