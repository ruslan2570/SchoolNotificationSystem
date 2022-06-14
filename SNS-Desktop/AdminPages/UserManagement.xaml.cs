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
	/// Interaction logic for UserManagement.xaml
	/// </summary>
	public partial class UserManagement : Page
	{
		private readonly HttpClient client = new HttpClient();

		List<User> usersList;
		List<Role> rolesList;

		string host;
		string sessionId;

		public UserManagement(string host, string sessionId)
		{
			InitializeComponent();
			this.host = host;
			this.sessionId = sessionId;
			LoadUsers();
			LoadRoles();
		}

		private async void LoadUsers()
		{
			await FillUsersList();
			lbUsers.ItemsSource = usersList;
		}

		private async void LoadRoles()
		{
			await FillRolesList();
			cbRole.ItemsSource = rolesList;
		}

		private async Task FillUsersList()
		{

			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=getUsers&session_id={sessionId}");
				result = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}

			JObject jObject = JObject.Parse(result);
			JToken tokenMessages = jObject["users"];
			JArray jarray = tokenMessages.ToObject<JArray>();
			usersList = jarray.ToObject<List<User>>();
		}

		private async Task FillRolesList()
		{
			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=getRoles&session_id={sessionId}");
				result = await client.GetStringAsync(uri);
			}
			catch (Exception ex)
			{
				MessageBox.Show("Ошибка соединения", "Ошибка");
				return;
			}

			JObject jObject = JObject.Parse(result);
			JToken tokenMessages = jObject["roles"];
			JArray jarray = tokenMessages.ToObject<JArray>();
			rolesList = jarray.ToObject<List<Role>>();
		}

		private async void btnAdd_Click(object sender, RoutedEventArgs e)
		{
			string username = tbLogin.Text;
			string password = pbPassword.Password;
			int roleId = ((Role)cbRole.SelectedItem).roleId;
			ClearData();

			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=addUser&session_id={sessionId}&username={username}&password={password}&role_id={roleId}");
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
			LoadUsers();
		}

		private async void btnDel_Click(object sender, RoutedEventArgs e)
		{
			btnDel.IsEnabled = false;
			User user = (User)lbUsers.SelectedItem;

			string result = "";
			client.DefaultRequestHeaders.Accept.Clear();
			client.DefaultRequestHeaders.Accept.Add(
			new MediaTypeWithQualityHeaderValue("application/json"));
			client.DefaultRequestHeaders.Add("User-Agent", "SchoolNotificationSystem desktop");
			try
			{
				Uri uri = new Uri($"http://{host}/request?action=delUser&session_id={sessionId}&user_id={user.userId}");
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
			LoadUsers();
		}

		private void Typing(object sender, EventArgs e)
		{
			if (String.IsNullOrWhiteSpace(tbLogin.Text) || String.IsNullOrWhiteSpace(pbPassword.Password) || cbRole.SelectedIndex == -1)
				btnAdd.IsEnabled = false;
			else
				btnAdd.IsEnabled = true;
		}

		private void lbUsers_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			if (lbUsers.SelectedIndex == -1)
				btnDel.IsEnabled = false;
			else
				btnDel.IsEnabled = true;
		}

		private void ClearData()
		{
			tbLogin.Text = "";
			pbPassword.Password = "";
			cbRole.SelectedIndex = -1;
		}
	}
}