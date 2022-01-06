package kh.monopoly;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.khoyte.monopoly.board.space.Tax;
import org.khoyte.monopoly.board.space.property.deed.Deed;
import org.khoyte.monopoly.board.space.property.deed.RailRoadDeed;
import org.khoyte.monopoly.board.space.property.deed.StreetDeed;
import org.khoyte.monopoly.board.space.property.deed.UtilityDeed;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
	public static void main(String[] args) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		File json = Paths.get("documentation/properties.json").toFile();

		JsonNode arrNode = mapper.readValue(json, JsonNode.class);

		List<StreetDeed> taxes = new ArrayList<>();

		if (arrNode.isArray()) {
			for (JsonNode obj : arrNode) {

				if (obj.findValue("type").asText().equals("property")) {
					taxes.add((StreetDeed) newStreetDeed(obj));
				}

			}
		}
	}

	static JsonNode rootJsonArrayNode() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File json = Paths.get("documentation/properties.json").toFile();
		JsonNode arrNode = mapper.readValue(json, JsonNode.class);

		return arrNode;
	}

	static UtilityDeed newUtilityDeed(JsonNode o) {

		String name = o.findValue("name").asText();
		int price = o.findValue("price").asInt();
		int mortgageValue = o.findValue("mortgage value").asInt();
		JsonNode rentWith = o.findValue("rent with");
		int oneOwned = rentWith.findValue("1 Utility owned").asInt();
		int twoOwned = rentWith.findValue("2 Utilities owned").asInt();

		return new UtilityDeed(name, price, mortgageValue, oneOwned, twoOwned);
	}

	static RailRoadDeed newRailroadDeed(JsonNode o) {

		String name = o.findValue("name").asText();
		int price = o.findValue("price").asInt();
		int mortgage_value = o.findValue("mortgage value").asInt();
		JsonNode rent = o.findValue("rent");
		int rent_1_rr_owned = rent.findValue("1 Railroad owned").asInt();
		int rent_2_rr_owned = rent.findValue("2 Railroads owned").asInt();
		int rent_3_rr_owned = rent.findValue("3 Railroads owned").asInt();
		int rent_4_rr_owned = rent.findValue("4 Railroads owned").asInt();

		return new RailRoadDeed(name, price, mortgage_value, rent_1_rr_owned, rent_2_rr_owned, rent_3_rr_owned,
				rent_4_rr_owned);
	}

	static Deed newStreetDeed(JsonNode p_node) {

		String name = p_node.findValue("name").asText();
		String color_group = p_node.findValue("color-group").asText();
		int price = p_node.findValue("price").asInt();
		int rent = p_node.findValue("rent").asInt();
		int building_costs = p_node.findValue("building costs").asInt();
		int mortgage_value = p_node.findValue("mortgage value").asInt();
		JsonNode rent_with = p_node.findValue("rent with");
		int rent_with_1_house = rent_with.findValue("1 House").asInt();
		int rent_with_2_houses = rent_with.findValue("2 Houses").asInt();
		int rent_with_3_houses = rent_with.findValue("3 Houses").asInt();
		int rent_with_4_houses = rent_with.findValue("4 Houses").asInt();
		int rent_with_1_hotel = findInt(rent_with, "1 Hotel");

		return new StreetDeed(name, color_group, price, rent, rent_with_1_house, rent_with_2_houses, rent_with_3_houses,
				rent_with_4_houses, rent_with_1_hotel, mortgage_value, building_costs, building_costs);
	}

	static Tax newTax(JsonNode o) {

		String name = o.findValue("name").asText();

		int payDollar = -1;
		if (o.hasNonNull("pay-dollar")) {
			payDollar = o.findValue("pay-dollar").asInt();
		}

		int payPercent = -1;
		if (o.hasNonNull("pay-percent")) {
			payPercent = o.findValue("pay-percent").asInt();
		}

		return new Tax(name, payDollar, payPercent);
	}

	static int findInt(JsonNode node, String name) {
		return node.findValue(name).asInt();
	}

}
