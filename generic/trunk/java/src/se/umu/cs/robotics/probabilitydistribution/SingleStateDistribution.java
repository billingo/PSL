package se.umu.cs.robotics.probabilitydistribution;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import se.umu.cs.robotics.collections.fuzzy.ConcreteFuzzyItem;
import se.umu.cs.robotics.collections.fuzzy.FuzzyItem;
import se.umu.cs.robotics.iteration.AbstractIterator;
import se.umu.cs.robotics.iteration.IterableIterator;
import se.umu.cs.robotics.statespace.StateDimension;

public class SingleStateDistribution<E> extends AbstractProbabilityDistribution<E> {

    private final E state;
    private final int stateIndex;

    public SingleStateDistribution(StateDimension<E> dimension, E state) {
        super(dimension);
        this.state = state;
        this.stateIndex = dimension.getIndex(state);
    }

    @Override
    public double getProbability(E state) {
        return 1d - getDimension().comparator().distance(this.state, state);
    }

    @Override
    public double getProbability(int index) {
        return getProbability(getDimension().getState(index));
    }

    @Override
    public double intersection(ProbabilityDistribution<E> pd) {
        return pd.getProbability(state);
    }

    public E getState() {
        return state;
    }

    public int getStateIndex() {
        return stateIndex;
    }

    @Override
    public String toString() {
        if (state instanceof Double) {
            return String.format(Locale.US,"{%.2f}", state);
        } else {
            return state.toString();
        }
    }

    public IterableIterator<FuzzyItem<E>> min() {
        return new IterableIterator<FuzzyItem<E>>() {

            private Iterator<E> dims = getDimension().iterator();
            private E next;

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                if (!dims.hasNext()) {
                    return false;
                } else if (next == null || next.equals(state)) {
                    next = dims.next();
                    return hasNext();
                } else {
                    return true;
                }
            }

            public FuzzyItem<E> next() {
                if (next == null && !hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    ConcreteFuzzyItem<E> item = new ConcreteFuzzyItem<E>(next, 0);
                    next = null;
                    return item;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public IterableIterator<FuzzyItem<E>> max() {
        return new IterableIterator<FuzzyItem<E>>() {

            private boolean hasNext = true;

            public Iterator<FuzzyItem<E>> iterator() {
                return this;
            }

            public boolean hasNext() {
                return hasNext;
            }

            public FuzzyItem<E> next() {
                hasNext = false;
                return new ConcreteFuzzyItem<E>(state, 1d);
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public boolean isUniform() {
        return false;
    }

    public IterableIterator<FuzzyItem<E>> nonZeroStates() {
        return new AbstractIterator<FuzzyItem<E>>() {

            private boolean hasNext = true;

            public boolean hasNext() {
                return hasNext;
            }

            public FuzzyItem<E> next() {
                if (hasNext) {
                    hasNext = false;
                    return new FuzzyItem<E>() {

                        public E element() {
                            return state;
                        }

                        public double value() {
                            return 1d;
                        }
                    };
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }
}
