package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import wooteco.chess.domain.piece.Piece;
import wooteco.chess.dto.PieceDto;
import wooteco.chess.util.PieceConverter;

public class PieceDao {
	private static final PieceDao PIECE_DAO = new PieceDao();
	private static final String TABLE_NAME = "piece";

	private PieceDao() {
	}

	public static PieceDao getInstance() {
		return PIECE_DAO;
	}

	public void add(PieceDto pieceDto) throws SQLException {
		String query = "INSERT INTO " + TABLE_NAME + " VALUES(?,?,?)";
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setString(1, pieceDto.getSymbol());
			pstmt.setString(2, pieceDto.getPosition());
			pstmt.setString(3, pieceDto.getTeam());
			pstmt.executeUpdate();
		}
	}

	public List<Piece> findAll() throws SQLException {
		String query = "SELECT * FROM " + TABLE_NAME;
		List<Piece> pieces = new ArrayList<>();
		ResultSet rs;
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String symbol = rs.getString("symbol");
				String position = rs.getString("position");
				Piece piece = PieceConverter.of(symbol, position);
				pieces.add(piece);
			}
		}
		return pieces;
	}

	public void update(String originalPosition, String newPosition) throws SQLException {
		String query = "UPDATE " + TABLE_NAME + " SET position = ? WHERE position = ?";
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.setString(1, newPosition);
			pstmt.setString(2, originalPosition);
			pstmt.executeUpdate();
		}
	}

	public void deleteAll() throws SQLException {
		String query = "DELETE FROM " + TABLE_NAME;
		try (
			Connection conn = DBConnector.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			pstmt.executeUpdate();
		}
	}
}
