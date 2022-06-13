using SNS_Desktop.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace SNS_Desktop.AdminPages
{
	/// <summary>
	/// Interaction logic for UserManagement.xaml
	/// </summary>
	public partial class UserManagement : Page
	{
		private readonly HttpClient client = new HttpClient();

		List<Message> list;

		string host;
		string sessionId;

		public UserManagement(string host, string sessionId)
		{
			InitializeComponent();
			this.host = host;
			this.sessionId = sessionId;
		}

	

		
	}
}
