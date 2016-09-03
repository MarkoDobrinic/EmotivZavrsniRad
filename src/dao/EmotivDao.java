package dao;

import helper.Constants;
import model.EmotivBaseline;
import model.EmotivBaselineMeasure;
import model.EmotivData;
import model.EmotivUser;

import java.sql.*;
import java.util.List;

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
            getConnection().commit();
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
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return baseline;
    }

    public void saveBaselineReading(List<EmotivData> allReadings, EmotivBaseline baseline) {
        for (EmotivData reading : allReadings) {
            saveBaselineReading(reading, baseline);
        }
    }

    public void saveBaselineReading(EmotivData emotivData, EmotivBaseline baseline) {

        PreparedStatement prepare = prepare(Constants.Database.BaselineMeasure.INSERT_BASELINE_MEASURE,
                baseline.getId(), emotivData.getNodeId(), emotivData.getAlpha(), emotivData.getBetaLow(),
                emotivData.getBetaHigh(), emotivData.getGamma(), emotivData.getTheta());
        try {
            prepare.execute();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public EmotivBaseline saveBasline(EmotivBaseline baseline) {

        PreparedStatement prepare = prepare(Constants.Database.Baseline.INSERT_BASELINE_BY_USER,
                baseline.getUserId());
        try {
            prepare.execute();
            getConnection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        EmotivUser user = new EmotivUser();
        user.setId(baseline.getUserId());
        return findBaselineByUser(user);
    }

    public EmotivBaselineMeasure getAverageMeasure(EmotivBaseline baseline) {

        EmotivBaselineMeasure baselineMeasure = new EmotivBaselineMeasure();
        PreparedStatement prepare = prepare(Constants.Database.BaselineMeasure.GET_AVERAGE_MEASURE, baseline.getId());

        try {
            ResultSet resultSet = prepare.executeQuery();
            baselineMeasure.setBaselineId(baseline.getId());
            baselineMeasure.setAlpha(resultSet.getDouble("ALPHA"));
            baselineMeasure.setBetaLow(resultSet.getDouble("BETA_LOW"));
            baselineMeasure.setBetaHigh(resultSet.getDouble("BETA_HIGH"));
            baselineMeasure.setGamma(resultSet.getDouble("GAMMA"));
            baselineMeasure.setTheta(resultSet.getDouble("THETA"));

            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return baselineMeasure;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.Database.DB_NAME);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public PreparedStatement prepare(String query, Object... params) {

        PreparedStatement stmt = null;
        try {
            stmt = getConnection().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                bindParam(stmt, i + 1, param);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }

    private void bindParam(PreparedStatement stmt, int i, Object param) throws SQLException {

        if (param instanceof Double) {
            stmt.setDouble(i, (Double) param);
        } else if (param instanceof String) {
            stmt.setString(i, (String) param);
        } else if (param instanceof Integer) {
            stmt.setInt(i, (Integer) param);
        }
    }


}
