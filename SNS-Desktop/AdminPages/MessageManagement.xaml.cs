using Newtonsoft.Json.Linq;
using SNS_Desktop.Model;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace SNS_Desktop.AdminPages
{
	/// <summary>
	/// Interaction logic for MessageManagement.xaml
	/// </summary>
	public partial class MessageManagement : Page
	{
		private readonly HttpClient client = new HttpClient();

		List<Message> list;

		string host;
		string sessionId;

		public MessageManagement(string host, string sessionId)
		{
			InitializeComponent();
			this.host = host;
			this.sessionId = sessionId;
			LoadMessages();
		}

		private async void LoadMessages()
		{
			await FillMessagesList();
			lbMessages.ItemsSource = list;
		}

		private async Task FillMessagesList()
		{
			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=getMessages&session_id={sessionId}");
				result = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}

			JObject jObject = JObject.Parse(result);
			JToken tokenMessages = jObject["messages"];
			JArray jarray = tokenMessages.ToObject<JArray>();
			list = jarray.ToObject<List<Message>>();
		}

		private void btnMsgShow_Click(object sender, RoutedEventArgs e)
		{
			Message msg = (Message)lbMessages.SelectedItem;
			if (msg == null)
			{
				MessageBox.Show("Выберите сообщение");
				return;
			}
			MessageBox.Show(msg.text, $"{msg.author} ({msg.role})");
		}

		private async void btnMsgDel_Click(object sender, RoutedEventArgs e)
		{
			Message msg = (Message)lbMessages.SelectedItem;
			if (msg == null)
			{
				MessageBox.Show("Выберите сообщение");
				return;
			}

			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=delMessage&session_id={sessionId}&message_id={msg.messageId}");
				result = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}
			JObject jObject = JObject.Parse(result);
			JToken tokenSuccess = jObject["success"];
			JToken tokenError = jObject["error"];
			string message = "";
			if (tokenSuccess != null)
				message = tokenSuccess.ToObject<string>();
			else if (tokenError != null)
				message = tokenError.ToObject<string>();
			MessageBox.Show(message, "Ответ");
			LoadMessages();
		}
	}
}