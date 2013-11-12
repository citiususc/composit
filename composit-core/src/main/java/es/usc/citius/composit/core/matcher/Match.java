package es.usc.citius.composit.core.matcher;


/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class Match<E,T> {
    private E matcher;
    private E matched;
    private T matchType;

    public Match(E matcher, E matched, T matchType) {
        if (matcher == null){
            throw new IllegalArgumentException("Matcher argument cannot be null");
        }
        this.matcher = matcher;
        if (matched == null){
            throw new IllegalArgumentException("Matched argument cannot be null");
        }
        this.matched = matched;
        if (matchType == null){
            throw new IllegalArgumentException("Match type argument cannot be null");
        }
        this.matchType = matchType;
    }

    public E getMatcher() {
        return matcher;
    }

    public E getMatched() {
        return matched;
    }

    public T getMatchType() {
        return matchType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (!matchType.equals(match.matchType)) return false;
        if (!matched.equals(match.matched)) return false;
        if (!matcher.equals(match.matcher)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = matcher.hashCode();
        result = 31 * result + matched.hashCode();
        result = 31 * result + matchType.hashCode();
        return result;
    }
}
