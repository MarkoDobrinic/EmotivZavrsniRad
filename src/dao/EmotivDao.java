package dao;

import helper.Constants;
import model.EmotivBaseline;
import model.EmotivData;
import model.EmotivUser;

import java.sql.*;

/**
 * Created by RedShift on 2.9.2016..
 */
public class EmotivDao {

    private Connection connection;

    public EmotivUser findUserByUsername(String username) {
        EmotivUser user = new EmotivUser();
        PreparedStatement prepare = prepare(Constants.Database.User.FIND_USER_BY_NAME, username);
        try {
            ResultSet resultSet = prepare.executeQuery();
            user.setId(resultSet.getInt(Constants.Database.User.ID));
            user.setUsername(resultSet.getString(Constants.Database.User.USERNAME));
            user.setPassword(resultSet.getString(Constants.Database.User.PASSWORD));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public EmotivBaseline findBaselineByUser(EmotivUser user) {

        PreparedStatement prepare = prepare(Constants.Database.Baseline.FIND_BASELINE_BY_USER, user.getId());
        EmotivBaseline baseline = new EmotivBaseline();
        try {
            ResultSet resultSet = prepare.executeQuery();
            baseline.setId(resultSet.getInt(Constants.Database.Baseline.ID));
            baseline.setUserId(resultSet.getInt(Constants.Database.Baseline.USER_ID));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return baseline;
    }

    public void saveBaselineReading(EmotivData emotivData, Integer baseId){

        PreparedStatement prepare = prepare(Constants.Database.BaselineMeasure.INSERT_BASELINE_MEASURE,
                baseId, emotivData.getNodeId(), emotivData.getAlpha(), emotivData.getBetaLow(),
                emotivData.getBetaHigh(), emotivData.getGamma(), emotivData.getTheta());
        try {
            prepare.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:"+ Constants.Database.DB_NAME);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public PreparedStatement prepare(String query, Object... params){

        PreparedStatement stmt = null;
        try {
            stmt = connect().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                bindParam(stmt, i, param);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    private void bindParam(PreparedStatement stmt, int i, Object param) throws SQLException {

        if (param instanceof Double) {
            stmt.setDouble(i, (Double) param);
        }else if (param instanceof String) {
            stmt.setString(i, (String) param);
        }else if(param instanceof Integer){
            stmt.setInt(i, (Integer) param);
        }
    }






}
