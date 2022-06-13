using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace SNS_Desktop
{
	/// <summary>
	/// Interaction logic for UserWindow.xaml
	/// </summary>
	public partial class UserWindow : Window
	{
		string sessionId;
		public UserWindow(string sessionId)
		{
			InitializeComponent();
			this.sessionId = sessionId;
		}

		private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
		{
			new AuthWindow().Show();
		}
	}
}
