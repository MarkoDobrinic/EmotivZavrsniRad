package dao;

import helper.Constants;
import model.*;

import java.sql.*;
import java.util.ArrayList;
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

    @SuppressWarnings("Duplicates")
    public EmotivTest findTestByBaseline(EmotivBaseline baseline) {

        PreparedStatement prepare = prepare(Constants.Database.Test.FIND_TEST_BY_BASELINE, baseline.getId());
        EmotivTest test = new EmotivTest();

        try {
            ResultSet resultSet = prepare.executeQuery();
            test.setId(resultSet.getInt(Constants.Database.Test.ID));
            test.setBaselineId(resultSet.getInt(Constants.Database.Test.BASELINE_ID));
            test.setDescription(resultSet.getString(Constants.Database.Test.DESCRIPTION));
            test.setGenre(resultSet.getString(Constants.Database.Test.GENRE));
            test.setSongname(resultSet.getString(Constants.Database.Test.SONGNAME));
            test.setSongDuration(resultSet.getDouble(Constants.Database.Test.SONGDURATION));
            test.setArtist(resultSet.getString(Constants.Database.Test.ARTIST));
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return test;
    }

    @SuppressWarnings("Duplicates")
    public EmotivTest findTestById(Integer id) {

        PreparedStatement prepare = prepare(Constants.Database.Test.FIND_TEST_BY_ID, id);
        EmotivTest test = new EmotivTest();

        try {
            ResultSet resultSet = prepare.executeQuery();
            test.setId(resultSet.getInt(Constants.Database.Test.ID));
            test.setBaselineId(resultSet.getInt(Constants.Database.Test.BASELINE_ID));
            test.setDescription(resultSet.getString(Constants.Database.Test.DESCRIPTION));
            test.setGenre(resultSet.getString(Constants.Database.Test.GENRE));
            test.setSongname(resultSet.getString(Constants.Database.Test.SONGNAME));
            test.setSongDuration(resultSet.getDouble(Constants.Database.Test.SONGDURATION));
            test.setArtist(resultSet.getString(Constants.Database.Test.ARTIST));
            test.setMeasures(findTestMeasuresByTestId(test.getId()));
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return test;
    }

    public List<EmotivTestMeasure> findTestMeasuresByTestId(Integer id){

        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_TEST_MEASURE_BY_TEST, id);
        List<EmotivTestMeasure> measureList = new ArrayList<>();

        try {
            getConnection().setAutoCommit(false);
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()){
                EmotivTestMeasure measure = new EmotivTestMeasure();
                measure.setBaselineId(resultSet.getInt(Constants.Database.TestMeasure.BASELINE_ID));
                measure.setTestId(resultSet.getInt(Constants.Database.TestMeasure.TEST_ID));
                measure.setNodeId(resultSet.getInt(Constants.Database.TestMeasure.NODE_ID));
                measure.setAlpha(resultSet.getDouble(Constants.Database.TestMeasure.ALPHA));
                measure.setBetaLow(resultSet.getDouble(Constants.Database.TestMeasure.BETALOW));
                measure.setBetaHigh(resultSet.getDouble(Constants.Database.TestMeasure.BETAHIGH));
                measure.setGamma(resultSet.getDouble(Constants.Database.TestMeasure.GAMMA));
                measure.setTheta(resultSet.getDouble(Constants.Database.TestMeasure.THETA));
                measure.setTime(resultSet.getInt(Constants.Database.TestMeasure.TIME));
                measureList.add(measure);
            }
            getConnection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return measureList;
    }


    public List<EmotivTest> findTestsByUser(EmotivUser user) {

        PreparedStatement prepare = prepare(Constants.Database.User.FIND_TEST_BY_USER, user.getId());
        List<EmotivTest> tests= new ArrayList<>();
        try {
            getConnection().setAutoCommit(false);
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()){

                EmotivTest test = new EmotivTest();
                test.setId(resultSet.getInt(Constants.Database.Test.ID));
                test.setBaselineId(resultSet.getInt(Constants.Database.Test.BASELINE_ID));
                test.setDescription(resultSet.getString(Constants.Database.Test.DESCRIPTION));
                test.setGenre(resultSet.getString(Constants.Database.Test.GENRE));
                test.setSongname(resultSet.getString(Constants.Database.Test.SONGNAME));
                test.setSongDuration(resultSet.getDouble(Constants.Database.Test.SONGDURATION));
                test.setArtist(resultSet.getString(Constants.Database.Test.ARTIST));
                test.setMeasures(findTestMeasuresByTestId(test.getId()));
                tests.add(test);
            }
            getConnection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public List<EmotivTest> findTestsWithAvgMeasuresByUser(EmotivUser user) {

        PreparedStatement prepare = prepare(Constants.Database.User.FIND_TEST_BY_USER, user.getId());
        List<EmotivTest> tests= new ArrayList<>();
        try {
            getConnection().setAutoCommit(false);
            ResultSet resultSet = prepare.executeQuery();
            while (resultSet.next()){

                EmotivTest test = new EmotivTest();
                test.setId(resultSet.getInt(Constants.Database.Test.ID));
                test.setBaselineId(resultSet.getInt(Constants.Database.Test.BASELINE_ID));
                test.setDescription(resultSet.getString(Constants.Database.Test.DESCRIPTION));
                test.setGenre(resultSet.getString(Constants.Database.Test.GENRE));
                test.setSongname(resultSet.getString(Constants.Database.Test.SONGNAME));
                test.setSongDuration(resultSet.getDouble(Constants.Database.Test.SONGDURATION));
                test.setArtist(resultSet.getString(Constants.Database.Test.ARTIST));
                test.setMeasures(findAvgWaveByTime(test));
                tests.add(test);
            }
            getConnection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    public Double findAvgAlphaByTestId(Integer id){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_AVG_ALPHA_BY_TEST_ID, id);
        Double avgWave = 0.0;
        try{
            ResultSet resultSet = prepare.executeQuery();
            avgWave = resultSet.getDouble("avgAlpha");
                //System.out.println("avgAlpha: " + resultSet.getDouble("avgBetaLow"));

            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("avgAlpha:" + avgWave);
        return avgWave;
    }
    public Double findAvgBetaLowByTestId(Integer id){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_AVG_BETALOW_BY_TEST_ID, id);
        Double avgWave = 0d;
        try{
            ResultSet resultSet = prepare.executeQuery();
            avgWave = resultSet.getDouble("avgBetaLow");
            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return avgWave;
    }
    public Double findAvgBetaHighByTestId(Integer id){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_AVG_BETAHIGH_BY_TEST_ID, id);
        Double avgWave = 0d;
        try{
            ResultSet resultSet = prepare.executeQuery();
            avgWave = resultSet.getDouble("avgBetaHigh");
            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return avgWave;
    }

    public Double findAvgGammaByTestId(Integer id){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_AVG_GAMMA_BY_TEST_ID, id);
        Double avgWave = 0d;
        try{
            ResultSet resultSet = prepare.executeQuery();
            avgWave = resultSet.getDouble("avgGamma");
            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return avgWave;
    }

    public Double findAvgThetaByTestId(Integer id){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_AVG_THETA_BY_TEST_ID, id);
        Double avgWave = 0d;
        try{
            ResultSet resultSet = prepare.executeQuery();
            avgWave = resultSet.getDouble("avgTheta");
            getConnection().commit();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return avgWave;
    }

    public List<EmotivTestMeasure> findAvgWaveByTime(EmotivTest test){
        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.FIND_TEST_MEASURE_AVG_BY_TIME, test.getId());
        List<EmotivTestMeasure> emotivDataList = new ArrayList<>();

        try {
            ResultSet resultSet = prepare.executeQuery();
            getConnection().setAutoCommit(false);
            while (resultSet.next()){
                EmotivTestMeasure measure = new EmotivTestMeasure();
                measure.setAlpha(resultSet.getDouble("avgAlpha"));
                measure.setBetaLow(resultSet.getDouble("avgBetaLow"));
                measure.setBetaHigh(resultSet.getDouble("avgBetaHigh"));
                measure.setGamma(resultSet.getDouble("avgGamma"));
                measure.setTheta(resultSet.getDouble("avgTheta"));
                measure.setTime(resultSet.getInt("TIME"));
                emotivDataList.add(measure);
                getConnection().commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emotivDataList;
    }



    public void saveBaselineReading(List<EmotivData> allReadings, EmotivBaseline baseline) {
        try {
            getConnection().setAutoCommit(false);
            for (EmotivData reading : allReadings) {
                saveBaselineReading(reading, baseline);
            }
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveBaselineReading(EmotivData emotivData, EmotivBaseline baseline) {

        PreparedStatement prepare = prepare(Constants.Database.BaselineMeasure.INSERT_BASELINE_MEASURE,
                baseline.getId(), emotivData.getNodeId(), emotivData.getAlpha(), emotivData.getBetaLow(),
                emotivData.getBetaHigh(), emotivData.getGamma(), emotivData.getTheta());
        try {
            prepare.execute();
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

    public EmotivTest saveTest(EmotivTest test) {

        PreparedStatement prepare = prepare(Constants.Database.Test.INSERT_TEST, test.getBaselineId(),
                test.getDescription(), test.getGenre(), test.getSongname(), test.getSongDuration(), test.getArtist());
        try {
            prepare.execute();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        EmotivBaseline baseline = new EmotivBaseline();
        baseline.setId(test.getBaselineId());
        return findTestByBaseline(baseline);

    }

    public void saveTestReading(EmotivTest test, EmotivData emotivData, EmotivBaseline baseline) {

        PreparedStatement prepare = prepare(Constants.Database.TestMeasure.INSERT_TEST_MEASURE,
                baseline.getId(), emotivData.getNodeId(), test.getId(), emotivData.getAlpha(), emotivData.getBetaLow(),
                emotivData.getBetaHigh(), emotivData.getGamma(), emotivData.getTheta(), emotivData.getTime());
        try {
            prepare.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTestReading(List<EmotivData> mainReadings, EmotivTest test, EmotivBaseline baseline) {
        try {
            getConnection().setAutoCommit(false);

            for (EmotivData reading : mainReadings) {
                saveTestReading(test, reading, baseline);
            }
            getConnection().commit();
            //getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String username, String password){

        PreparedStatement prepare = prepare(Constants.Database.User.INSERT_USER, username, password);
        try {
            prepare.execute();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("User added to database.");
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

    public boolean deleteUser(String username){

        PreparedStatement prepare = prepare(Constants.Database.User.DELETE_USER, username);

        try {
            prepare.execute();
            getConnection().commit();
            System.out.println("User successfully deleted.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User cannot be deleted.");

        return false;
    }

    public void deleteTest(Integer id){

        PreparedStatement prepare = prepare(Constants.Database.User.DELETE_TEST, id);

        try {
            prepare.execute();
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Test successfully deleted.");
    }

    public void findLastTest(){

        PreparedStatement prepare = prepare(Constants.Database.Test.FIND_LAST_TEST);
        try {
            ResultSet resultSet = prepare.executeQuery();
            EmotivContext.TEST.setId(resultSet.getInt("ID"));
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
