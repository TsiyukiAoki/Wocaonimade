import lombok.*;

import java.lang.reflect.Field;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
class DataClass<T> {

    public DataClass(T o) {
        Arrays.stream(getClass().getDeclaredFields())
                .forEach(f -> {
                            try {
                                Field of = getDeclaredFieldRecur(o.getClass(), f.getName());
                                of.setAccessible(true);
                                f.set(this, of.get(o));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
    }

    @Override
    public boolean equals(Object o) {
        return Arrays.stream(getClass().getDeclaredFields())
                .allMatch(f -> {
                    try {
                        val of = getDeclaredFieldRecur(o.getClass(), f.getName());
                        of.setAccessible(true);
                        return Objects.isNull(f.get(this)) || Objects.equals(f.get(this), of.get(o));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                });
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private static Field getDeclaredFieldRecur(Class<?> clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (Object.class.equals(clazz)) {
                throw e;
            }
            return getDeclaredFieldRecur(clazz.getSuperclass(), name);
        }
    }
}

@Builder
@AllArgsConstructor
class DCourse extends DataClass<Course> {

    String cid;

    String name;

    Integer credit;

    GradingSchema gradingSchema;

    Integer capacity;

    Integer leftCapacity;

    CollectionExt.MockSet<DTimeslot> timeslots;

    public DCourse(Course o) {
        super(o);
    }
}

@Builder
@AllArgsConstructor
class DTimeslot extends DataClass<Timeslot> {

    DayOfWeek dayOfWeek;

    Time start;

    Time end;

    public DTimeslot(Timeslot o) {
        super(o);
    }
}

@Builder
@AllArgsConstructor
class DStudent extends DataClass<Student> {

    Integer id;

    String name;

    CollectionExt.MockMap<DCourse, Grade> courses;

    public DStudent(Student o) {
        super(o);
    }
}

class CollectionExt {

    @SafeVarargs
    public static <T> MockSet<T> asSet(T... args) {
        return new MockSet<T>(Stream.of(args).collect(Collectors.toSet()));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> MockMap<K, V> asMap(Object... es) {
        assert es.length % 2 == 0;
        val res = new HashMap<K, V>(es.length / 2);
        for (int i = 0; i < es.length; i += 2) {
            res.put((K) es[i], (V) es[i + 1]);
        }
        return new MockMap<>(res);
    }

    static class MockSet<T> extends HashSet<T> {

        public MockSet(Collection<T> o) {
            super(o);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (Objects.isNull(o) || !(o instanceof Set)) {
                return false;
            }
            val os = (Set<T>) o;
            for (val e : this) {
                if (os.stream().noneMatch(e::equals)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class MockMap<K, V> extends HashMap<K, V> {

        public MockMap(Map<? extends K, ? extends V> o) {
            super(o);
        }

        @Override
        public boolean containsKey(Object key) {
            return keySet().stream().anyMatch(k -> k.equals(key));
        }

        @Override
        public V get(Object key) {
            return entrySet().stream()
                    .filter(e -> e.getKey().equals(key))
                    .map(Entry::getValue)
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (Objects.isNull(o) || !(o instanceof Map)) {
                return false;
            }
            val om = (Map<K, V>) o;
            if (size() != om.size()) {
                return false;
            }
            for (val k : om.keySet()) {
                if (!containsKey(k) || !Objects.equals(get(k), om.get(k))) {
                    return false;
                }
            }
            return true;
        }
    }
}
