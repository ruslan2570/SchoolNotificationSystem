using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SNS_Desktop.Model
{
	internal class Message
	{
		public int messageId { get; set; }
		public string text { get; set; }
		public string author { get; set; }
		public string role { get; set; }
	}
}
