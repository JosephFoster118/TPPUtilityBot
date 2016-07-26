package utility.tpp.tpputilitybot;

/**
 * Created by joseph on 7/24/16.
 */
public class Pokemon
{
    private String move_a;
    private String move_b;
    private String move_c;
    private String move_d;

    Pokemon(String a, String b, String c, String d)
    {
        move_a = a;
        move_b = b;
        move_c = c;
        move_d = d;
    }

    String getMoveA()
    {
        return move_a;
    }

    String getMoveB()
    {
        return move_b;
    }

    String getMoveC()
    {
        return move_c;
    }

    String getMoveD()
    {
        return move_d;
    }

}
