package network;

import retrofit2.Retrofit;

public class Network implements INetwork {

	private String url;

	public Network(String url) {
		this.url = url;
	}

	@Override
	public String getData() throws Exception {

		Retrofit retrofit = new Retrofit.Builder().baseUrl(url).build();

		TableSoccerService service = retrofit.create(TableSoccerService.class);

		return service.getData().execute().body();

	}

}
